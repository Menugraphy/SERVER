package com.menugraphy.server.global.external.storage;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.CannedAccessControlList;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.drew.imaging.ImageMetadataReader;
import com.drew.metadata.Metadata;
import com.drew.metadata.exif.GpsDirectory;
import com.menugraphy.server.domain.member.model.entity.Member;
import com.menugraphy.server.domain.member.repository.MemberRepository;
import com.menugraphy.server.domain.menu.model.vo.ImageNameExtension;
import com.menugraphy.server.global.auth.PrincipalHandler;
import com.menugraphy.server.global.exception.CustomException;
import com.menugraphy.server.global.exception.ErrorType;
import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
@RequiredArgsConstructor
@Slf4j
public class StorageService {

    private final AmazonS3 amazonS3;
    private final MemberRepository memberRepository;
    private final PrincipalHandler principalHandler;

    @Value("${cloud.aws.s3.bucket}")
    private String bucketName;

    private static final List<String> ALLOWED_FILE_EXTENSIONS = Arrays.asList("jpg", "jpeg", "png", "heic");

    public ImageNameExtension uploadFile(MultipartFile file) {
        Member member = memberRepository.findMemberByIdOrThrow(principalHandler.getUserIdFromPrincipal());

        // 파일 확장자 체크
        String fileName = file.getOriginalFilename();
        if (file.getOriginalFilename() == null || !isAllowedExtension(fileName)) {
            throw new IllegalArgumentException("허용되지 않는 파일 형식입니다. 허용되는 형식: jpg, jpeg, png, heic");
        }

        fileName = UUID.randomUUID() + "-" + member.getId() + "-" + fileName;

        // 메타데이터 설정
        ObjectMetadata metadata = new ObjectMetadata();
        metadata.setContentLength(file.getSize());
        metadata.setContentType(file.getContentType());

        Double latitude = null;
        Double longitude = null;

        // 이미지 파일에서 GPS 정보 추출
        try (InputStream inputStream = file.getInputStream()) {
            Metadata imageMetadata = ImageMetadataReader.readMetadata(inputStream);
            GpsDirectory gpsDirectory = imageMetadata.getFirstDirectoryOfType(GpsDirectory.class);

            if (gpsDirectory != null && gpsDirectory.getGeoLocation() != null) {
                latitude = gpsDirectory.getGeoLocation().getLatitude();
                longitude = gpsDirectory.getGeoLocation().getLongitude();
                // 위도 경도 값을 메타데이터에 추가
                metadata.addUserMetadata("latitude", String.valueOf(latitude));
                metadata.addUserMetadata("longitude", String.valueOf(longitude));
            }
        } catch (Exception e) {
            log.warn("이미지 파일에서 GPS 정보를 추출하는데 실패했습니다. 파일: {}", fileName);
        }

        // S3에 파일 업로드
        String key = "OCR_before/" + fileName;

        try {
            PutObjectRequest putObjectRequest = new PutObjectRequest(bucketName, key, file.getInputStream(), metadata)
                    .withCannedAcl(CannedAccessControlList.PublicRead);
            amazonS3.putObject(putObjectRequest);
        } catch (IOException e) {
            throw new CustomException(ErrorType.S3_UPLOAD_ERROR);
        }

        return ImageNameExtension.of(
                extractFileNameWithoutExtension(fileName),
                getFileExtension(fileName),
                amazonS3.getUrl(bucketName, key).toString(),
                latitude,
                longitude
        );
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

    public String extractFileNameWithoutExtension(String fileName) {
        int lastDotIndex = fileName.lastIndexOf(".");

        return fileName.substring(0, lastDotIndex);
    }
}
