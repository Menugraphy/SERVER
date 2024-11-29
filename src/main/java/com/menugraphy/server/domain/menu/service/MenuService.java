package com.menugraphy.server.domain.menu.service;

import com.amazonaws.services.s3.AmazonS3;
import com.menugraphy.server.domain.food.model.entity.Food;
import com.menugraphy.server.domain.food.model.entity.Type;
import com.menugraphy.server.domain.food.repository.FoodRepository;
import com.menugraphy.server.domain.food.repository.TypeRepository;
import com.menugraphy.server.domain.member.model.entity.Member;
import com.menugraphy.server.domain.member.model.enums.ScriptType;
import com.menugraphy.server.domain.member.repository.MemberRepository;
import com.menugraphy.server.domain.menu.model.dto.ImageRequest;
import com.menugraphy.server.domain.menu.model.dto.ImageResponse;
import com.menugraphy.server.domain.menu.model.dto.MenuDetailResponse;
import com.menugraphy.server.domain.menu.model.dto.MenuListResponse;
import com.menugraphy.server.domain.menu.model.dto.MenuResponse;
import com.menugraphy.server.domain.menu.model.dto.OrderScriptListRequest;
import com.menugraphy.server.domain.menu.model.dto.OrderScriptRequest;
import com.menugraphy.server.domain.menu.model.dto.OrderScriptResponse;
import com.menugraphy.server.domain.menu.model.entity.MenuBoard;
import com.menugraphy.server.domain.menu.model.vo.MenuBoardImage;
import com.menugraphy.server.domain.menu.model.vo.SimilarFood;
import com.menugraphy.server.domain.menu.model.vo.TypeName;
import com.menugraphy.server.domain.menu.repository.MenuBoardRepository;
import com.menugraphy.server.global.auth.PrincipalHandler;
import com.menugraphy.server.global.exception.CustomException;
import com.menugraphy.server.global.exception.ErrorType;
import com.menugraphy.server.global.external.storage.StorageService;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class MenuService {

    private final MenuBoardRepository menuBoardRepository;
    private final FoodRepository foodRepository;
    private final StorageService storageService;

    private final AmazonS3 amazonS3;
    private final MemberRepository memberRepository;
    private final PrincipalHandler principalHandler;

    @Value("${cloud.aws.s3.bucket}")
    private String bucketName;

    private final BigDecimal exchangeRate = new BigDecimal("1397");
    private final TypeRepository typeRepository;

    @Transactional
    public ImageResponse uploadImage(
            ImageRequest imageRequest
    ) {
        Member member = memberRepository.findMemberByIdOrThrow(principalHandler.getUserIdFromPrincipal());
        String fileName = member.getId() + "-" + imageRequest.image().getOriginalFilename();
        String fileUrl = amazonS3.getUrl(bucketName, "OCR_after/" + fileName).toString();

        if (menuBoardRepository.existsByAfterImage(fileUrl)) {
            MenuBoard menuBoard = menuBoardRepository.findByAfterImageByIdOrThrow(fileUrl);

            return ImageResponse.of(menuBoard.getId(), fileUrl);
        } else {
            throw new CustomException(ErrorType.S3_UPLOAD_ERROR);
        }
    }

    @Transactional
    public void saveMenuBoard(
            ImageRequest imageRequest
    ) {
        MenuBoardImage menuBoardImage = storageService.uploadFile(imageRequest.image());

        MenuBoard menuBoard = MenuBoard.builder()
                .beforeImage(menuBoardImage.beforeImage())
                .afterImage(menuBoardImage.afterImage())
                .build();

        menuBoardRepository.save(menuBoard);
    }

    @Transactional(readOnly = true)
    public MenuListResponse restructureMenuBoard(
            Long imageId
    ) {
        MenuBoard menuBoard = menuBoardRepository.findMenuBoardByIdOrThrow(imageId);

        final BigDecimal exchangeRate = new BigDecimal("1397");

        List<MenuResponse> menuResponseList = menuBoard.getMenuPriceList().stream()
                .filter(menuPrice -> foodRepository.existsByName(menuPrice.menuName()))
                .map(menuPrice -> {
                    Food food = foodRepository.findFoodByNameOrThrow(menuPrice.menuName());

                    BigDecimal priceInWon = new BigDecimal(menuPrice.price());
                    BigDecimal priceInUsd = priceInWon.divide(exchangeRate, 2, RoundingMode.HALF_UP);
                    String priceInUsdFormatted = priceInUsd + " USD";

                    return MenuResponse.of(
                            food.getId(),
                            food.getImage(),
                            food.getName(),
                            food.getDescription(),
                            Integer.parseInt(menuPrice.price()),
                            priceInUsdFormatted
                    );
                })
                .collect(Collectors.toList());

        return MenuListResponse.of(menuResponseList);
    }

    @Transactional(readOnly = true)
    public MenuDetailResponse fetchMenuDetail(
            Long menuId
    ) {
        Food food = foodRepository.findFoodByIdOrThrow(menuId);
        List<TypeName> foodTypeList = new ArrayList<>();
        List<SimilarFood> similarFoodList = new ArrayList<>();

        for (int i = 0; i < food.getFoodTypeList().size(); i++) {
            Type type = typeRepository.findTypeByOrThrow(food.getFoodTypeList().get(i));
            TypeName typeName = TypeName.of(type.getName());
            foodTypeList.add(typeName);
        }

        for (int i = 0; i < food.getSimilarFoodList().size(); i++) {
            Food similarFood = foodRepository.findFoodByIdOrThrow(food.getSimilarFoodList().get(i));
            SimilarFood sf = SimilarFood.of(similarFood.getImage(), similarFood.getName());
            similarFoodList.add(sf);
        }

        return MenuDetailResponse.of(
                food.getId(),
                food.getImage(),
                foodTypeList,
                food.getName(),
                food.getDescription(),
                similarFoodList
        );
    }

    @Transactional(readOnly = true)
    public OrderScriptResponse fetchOrderScript(OrderScriptListRequest orderScriptListRequest) {
        String korean = buildScript(orderScriptListRequest, ScriptType.KOREAN);
        String romanized = buildScript(orderScriptListRequest, ScriptType.ROMANIZED);
        String translatedText = buildScript(orderScriptListRequest, ScriptType.TRANSLATED);

        return OrderScriptResponse.of(korean, romanized, translatedText);
    }

    private String buildScript(OrderScriptListRequest orderScriptListRequest, ScriptType type) {
        StringBuilder script = new StringBuilder();

        for (int i = 0; i < orderScriptListRequest.menuOrderList().size(); i++) {
            OrderScriptRequest orderScriptRequest = orderScriptListRequest.menuOrderList().get(i);
            String menuName = orderScriptRequest.menuName();
            int menuCount = orderScriptRequest.menuCount();

            switch (type) {
                case KOREAN -> script.append(menuName)
                        .append(" ")
                        .append(menuCount)
                        .append("인분");
                case ROMANIZED -> script.append(menuName)
                        .append(" ")
                        .append(menuCount)
                        .append("inbun");
                case TRANSLATED -> script.append("I'd like to order ")
                        .append(menuCount)
                        .append(" servings of ")
                        .append(menuName);
            }

            if (i == orderScriptListRequest.menuOrderList().size() - 1) {
                script.append(type.getSuffix());
            } else {
                script.append(type.getSeparator());
            }
        }

        return script.toString();
    }
}
