package com.menugraphy.server.global.external.storage;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.CannedAccessControlList;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.menugraphy.server.domain.member.model.entity.Member;
import com.menugraphy.server.domain.member.repository.MemberRepository;
import com.menugraphy.server.domain.menu.model.vo.MenuBoardImage;
import com.menugraphy.server.global.auth.PrincipalHandler;
import com.menugraphy.server.global.exception.CustomException;
import com.menugraphy.server.global.exception.ErrorType;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
@RequiredArgsConstructor
public class StorageService {

    private final AmazonS3 amazonS3;
    private final MemberRepository memberRepository;
    private final PrincipalHandler principalHandler;

    @Value("${cloud.aws.s3.bucket}")
    private String bucketName;

    private static final List<String> ALLOWED_FILE_EXTENSIONS = Arrays.asList("jpg", "jpeg", "png", "heic");

    public MenuBoardImage uploadFile(MultipartFile file) {
        Member member = memberRepository.findMemberByIdOrThrow(principalHandler.getUserIdFromPrincipal());

        // 파일 확장자 체크
        String fileName = file.getOriginalFilename();
        if (file.getOriginalFilename() == null || !isAllowedExtension(fileName)) {
            throw new IllegalArgumentException("허용되지 않는 파일 형식입니다. 허용되는 형식: jpg, jpeg, png, heic");
        }

        fileName = member.getId() + "-" + fileName;

        // 메타데이터 설정
        ObjectMetadata metadata = new ObjectMetadata();
        metadata.setContentLength(file.getSize());
        metadata.setContentType(file.getContentType());

        // S3에 파일 업로드
        String key = "OCR_before/" + fileName;

        try {
            PutObjectRequest putObjectRequest = new PutObjectRequest(bucketName, key, file.getInputStream(), metadata)
                    .withCannedAcl(CannedAccessControlList.PublicRead);
            amazonS3.putObject(putObjectRequest);
        } catch (IOException e) {
            throw new CustomException(ErrorType.S3_UPLOAD_ERROR);
        }

        String beforeUrl = amazonS3.getUrl(bucketName, key).toString();
        String afterUrl = amazonS3.getUrl(bucketName, "OCR_after/" + fileName).toString();

        // 업로드된 파일의 URL 반환
        return MenuBoardImage.of(beforeUrl, afterUrl);
    }

    private boolean isAllowedExtension(String fileName) {
        String extension = getFileExtension(fileName).toLowerCase();
        return ALLOWED_FILE_EXTENSIONS.contains(extension);
    }

    private String getFileExtension(String fileName) {
        int lastDotIndex = fileName.lastIndexOf('.');
        if (lastDotIndex == -1) {
            return "";
        }
        return fileName.substring(lastDotIndex + 1);
    }
}
