package com.menugraphy.server.global.external.client.service;

import com.google.api.client.googleapis.auth.oauth2.GoogleIdToken;
import com.google.api.client.googleapis.auth.oauth2.GoogleIdTokenVerifier;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.gson.GsonFactory;
import com.menugraphy.server.domain.member.model.enums.SocialType;
import com.menugraphy.server.global.exception.CustomException;
import com.menugraphy.server.global.exception.ErrorType;
import com.menugraphy.server.global.external.client.dto.MemberInfoResponse;
import jakarta.annotation.PostConstruct;
import java.io.IOException;
import java.security.GeneralSecurityException;
import java.util.Arrays;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Slf4j
public class GoogleSocialService {

    @Value("${google.serverClientId}")
    private String serverClientId;

    @Value("${google.iOSClientId}")
    private String iOSClientId;

    @Value("${google.androidClientId}")
    private String androidClientId;

    private final NetHttpTransport transport = new NetHttpTransport();
    private final GsonFactory jsonFactory = GsonFactory.getDefaultInstance();
    private GoogleIdTokenVerifier verifier;

    @PostConstruct
    public void initVerifier() {
        if (serverClientId == null || iOSClientId == null || androidClientId == null) {
            log.error(
                    "One or more Google client IDs are not properly configured.\n"
                            + "ServerClientId: {}, iOSClientId: {}, AndroidClientId: {}",
                    serverClientId, iOSClientId, androidClientId);
            throw new CustomException(ErrorType.VALIDATION_ERROR);
        }
        verifier = new GoogleIdTokenVerifier.Builder(transport, jsonFactory)
                .setAudience(Arrays.asList(serverClientId, iOSClientId, androidClientId))
                .build();
    }

    @Transactional
    public MemberInfoResponse login(
            final SocialType socialType,
            final String idTokenString
    ) {
        if (idTokenString == null || idTokenString.isEmpty()) {
            log.error("ID Token이 비어 있습니다.");
            throw new CustomException(ErrorType.INVALID_ID_TOKEN_ERROR);
        }

        GoogleIdToken idToken = verifyIdToken(idTokenString);

        if (idToken == null) {
            log.error("ID Token이 검증되지 않았습니다. 토큰 문자열: {}", idTokenString);
            throw new CustomException(ErrorType.INVALID_ID_TOKEN_ERROR);
        }

        GoogleIdToken.Payload payload = idToken.getPayload();
        String userId = payload.getSubject();

        if (userId == null || userId.isEmpty()) {
            log.error("ID Token에서 사용자 ID를 찾을 수 없습니다. Payload: {}", payload);
            throw new CustomException(ErrorType.INVALID_ID_TOKEN_ERROR);
        }

        log.info("Google 로그인 성공. 사용자 ID: {}, 이메일: {}", userId, payload.getEmail());
        return MemberInfoResponse.of(socialType, userId);
    }

    private GoogleIdToken verifyIdToken(final String idTokenString) {
        try {
            GoogleIdToken idToken = verifier.verify(idTokenString);
            if (idToken == null) {
                log.warn("Google ID Token 검증 실패: 토큰이 유효하지 않거나 만료되었습니다.");
            }
            return idToken;
        } catch (GeneralSecurityException e) {
            log.error("ID Token 검증 중 보안 예외 발생. 토큰: {}. 에러: {}", idTokenString, e.getMessage(), e);
            throw new CustomException(ErrorType.INVALID_ID_TOKEN_ERROR);
        } catch (IOException e) {
            log.error("Google Public Key 다운로드 실패. 네트워크 문제일 수 있습니다. 에러: {}", e.getMessage(), e);
            throw new CustomException(ErrorType.FAILED_DOWNLOAD_GOOGLE_PUBLIC_KEY_ERROR);
        }
    }
}
