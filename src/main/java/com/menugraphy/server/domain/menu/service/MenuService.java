package com.menugraphy.server.domain.menu.service;

import com.amazonaws.services.s3.AmazonS3;
import com.menugraphy.server.domain.member.model.entity.Member;
import com.menugraphy.server.domain.member.repository.MemberRepository;
import com.menugraphy.server.domain.menu.model.dto.ImageRequest;
import com.menugraphy.server.domain.menu.model.dto.ImageResponse;
import com.menugraphy.server.domain.menu.model.entity.MenuBoard;
import com.menugraphy.server.domain.menu.repository.MenuBoardRepository;
import com.menugraphy.server.global.auth.PrincipalHandler;
import com.menugraphy.server.global.exception.CustomException;
import com.menugraphy.server.global.exception.ErrorType;
import com.menugraphy.server.global.external.storage.StorageService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class MenuService {

    private final MenuBoardRepository menuBoardRepository;
    private final StorageService storageService;

    private final AmazonS3 amazonS3;
    private final MemberRepository memberRepository;
    private final PrincipalHandler principalHandler;

    @Value("${cloud.aws.s3.bucket}")
    private String bucketName;

    @Transactional
    public ImageResponse uploadImage(
            ImageRequest imageRequest
    ) {
        Member member = memberRepository.findMemberByIdOrThrow(principalHandler.getUserIdFromPrincipal());
        String fileName = member.getId() + "-" + imageRequest.image().getOriginalFilename();
        String fileUrl = amazonS3.getUrl(bucketName, "OCR_After/" + fileName).toString();

        if (menuBoardRepository.existsByImage(fileUrl)) {
            MenuBoard menuBoard = menuBoardRepository.findByImageByIdOrThrow(fileUrl);

            return ImageResponse.of(menuBoard.getId(), fileUrl);
        } else {
            throw new CustomException(ErrorType.S3_UPLOAD_ERROR);
        }
    }

    @Transactional
    public void saveMenuBoard(
            ImageRequest imageRequest
    ) {
        String fileUrl = storageService.uploadFile(imageRequest.image());

        MenuBoard menuBoard = MenuBoard.builder()
                .image(fileUrl)
                .build();

        menuBoardRepository.save(menuBoard);
    }
}
