package com.menugraphy.server.domain.menu.service;

import com.menugraphy.server.domain.food.model.entity.Food;
import com.menugraphy.server.domain.food.model.entity.Type;
import com.menugraphy.server.domain.food.repository.FoodRepository;
import com.menugraphy.server.domain.food.repository.TypeRepository;
import com.menugraphy.server.domain.member.model.entity.Member;
import com.menugraphy.server.domain.member.model.enums.ScriptType;
import com.menugraphy.server.domain.member.repository.FoodAvoidanceRepository;
import com.menugraphy.server.domain.member.repository.MemberRepository;
import com.menugraphy.server.domain.menu.model.MenuPriceMapper;
import com.menugraphy.server.domain.menu.model.dto.ImageRequest;
import com.menugraphy.server.domain.menu.model.dto.ImageResponse;
import com.menugraphy.server.domain.menu.model.dto.MenuDetailResponse;
import com.menugraphy.server.domain.menu.model.dto.MenuListResponse;
import com.menugraphy.server.domain.menu.model.dto.MenuResponse;
import com.menugraphy.server.domain.menu.model.dto.OrderScriptListRequest;
import com.menugraphy.server.domain.menu.model.dto.OrderScriptRequest;
import com.menugraphy.server.domain.menu.model.dto.OrderScriptResponse;
import com.menugraphy.server.domain.menu.model.entity.MenuBoard;
import com.menugraphy.server.domain.menu.model.vo.ImageNameExtension;
import com.menugraphy.server.domain.menu.model.vo.MenuPrice;
import com.menugraphy.server.domain.menu.model.vo.SimilarFood;
import com.menugraphy.server.domain.menu.model.vo.TypeName;
import com.menugraphy.server.domain.menu.repository.MenuBoardRepository;
import com.menugraphy.server.global.auth.PrincipalHandler;
import com.menugraphy.server.global.exception.CustomException;
import com.menugraphy.server.global.exception.ErrorType;
import com.menugraphy.server.global.external.ai.dto.MenuResultResponse;
import com.menugraphy.server.global.external.ai.service.MenuOcrService;
import com.menugraphy.server.global.external.storage.StorageService;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class MenuService {

    private final MenuBoardRepository menuBoardRepository;
    private final FoodRepository foodRepository;
    private final StorageService storageService;
    private final MenuOcrService menuOcrService;

    private final MemberRepository memberRepository;
    private final FoodAvoidanceRepository foodAvoidanceRepository;
    private final PrincipalHandler principalHandler;

    private final static BigDecimal exchangeRate = new BigDecimal("1397");
    private final TypeRepository typeRepository;

    private final MenuPriceMapper menuPriceMapper;

    @Transactional
    public ImageResponse uploadImage(
            ImageRequest imageRequest
    ) {
        ImageNameExtension imageNameExtension = storageService.uploadFile(imageRequest.image());

        String resultPath = menuOcrService.fetchResultPath(imageNameExtension.key(), imageNameExtension.extension());

        List<MenuResultResponse> menuResultResponses = menuOcrService.fetchMenuResult(imageNameExtension.key());

        List<MenuPrice> menuPrices = menuResultResponses.stream()
                .map(menuPriceMapper::toMenuPrice)
                .toList();

        MenuBoard menuBoard = MenuBoard.builder()
                .beforeImage(imageNameExtension.key())
                .afterImage(resultPath)
                .menuPriceList(menuPrices)
                .build();

        menuBoardRepository.save(menuBoard);

        if (menuBoardRepository.existsByAfterImage(menuBoard.getAfterImage())) {
            return ImageResponse.of(menuBoard.getId(), menuBoard.getAfterImage());
        } else {
            throw new CustomException(ErrorType.S3_UPLOAD_ERROR);
        }
    }

    @Transactional(readOnly = true)
    public MenuListResponse restructureMenuBoard(Long imageId) {
        Member member = memberRepository.findMemberByIdOrThrow(principalHandler.getUserIdFromPrincipal());
        MenuBoard menuBoard = menuBoardRepository.findMenuBoardByIdOrThrow(imageId);

        List<MenuResponse> menuResponseList = new ArrayList<>();

        for (MenuPrice menuPrice : menuBoard.getMenuPriceList()) {
            if (foodRepository.existsById(menuPrice.foodId())) {
                Food food = foodRepository.findFoodByIdOrThrow(menuPrice.foodId());

                BigDecimal priceInWon = new BigDecimal(menuPrice.price());
                BigDecimal priceInUsd = priceInWon.divide(exchangeRate, 2, RoundingMode.HALF_UP);
                String priceInUsdFormatted = priceInUsd + " USD";

                MenuResponse menuResponse = MenuResponse.of(
                        food.getId(),
                        food.getImage(),
                        food.getName(),
                        food.getDescription(),
                        Integer.parseInt(menuPrice.price()),
                        priceInUsdFormatted,
                        isAvoidanceFood(member, food)
                );

                menuResponseList.add(menuResponse);
            }
        }

        return MenuListResponse.of(menuResponseList);
    }

    private boolean isAvoidanceFood(Member member, Food food) {
        Set<Long> avoidanceTypeIds = food.getFoodTypeList().stream()
                .map(typeRepository::findTypeByIdOrThrow)
                .map(Type::getId)
                .collect(Collectors.toSet());

        return avoidanceTypeIds.stream()
                .anyMatch(typeId -> foodAvoidanceRepository.existsByMemberIdAndTypeId(member.getId(), typeId));
    }

    @Transactional(readOnly = true)
    public MenuDetailResponse fetchMenuDetail(Long menuId) {
        Food food = foodRepository.findFoodByIdOrThrow(menuId);

        List<TypeName> foodTypeList = food.getFoodTypeList().stream()
                .map(typeRepository::findTypeByIdOrThrow)
                .map(type -> TypeName.of(type.getName()))
                .toList();

        List<SimilarFood> similarFoodList = food.getSimilarFoodList().stream()
                .map(foodRepository::findFoodByIdOrThrow)
                .map(similarFood -> SimilarFood.of(similarFood.getImage(), similarFood.getName()))
                .toList();

        // Response 생성 및 반환
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
