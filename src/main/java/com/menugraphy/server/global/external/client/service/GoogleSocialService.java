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
import java.util.Collections;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Slf4j
public class GoogleSocialService {

    @Value("${google.clientId}")
    private String clientId;

    private final NetHttpTransport transport = new NetHttpTransport();
    private final GsonFactory jsonFactory = GsonFactory.getDefaultInstance();
    private GoogleIdTokenVerifier verifier;

    @PostConstruct
    public void initVerifier() {
        verifier = new GoogleIdTokenVerifier.Builder(transport, jsonFactory)
                .setAudience(Collections.singletonList(clientId))
                .build();
    }

    @Transactional
    public MemberInfoResponse login(
            final SocialType socialType,
            final String idTokenString
    ) {
        GoogleIdToken idToken = verifyIdToken(idTokenString);

        if (idToken == null) {
            throw new CustomException(ErrorType.INVALID_ID_TOKEN_ERROR);
        }

        GoogleIdToken.Payload payload = idToken.getPayload();
        String userId = payload.getSubject();

        return MemberInfoResponse.of(socialType, userId);
    }

    private GoogleIdToken verifyIdToken(final String idTokenString) {
        try {
            return verifier.verify(idTokenString);
        } catch (GeneralSecurityException e) {
            throw new CustomException(ErrorType.INVALID_ID_TOKEN_ERROR);
        } catch (IOException e) {
            throw new CustomException(ErrorType.FAILED_DOWNLOAD_GOOGLE_PUBLIC_KEY_ERROR);
        }
    }
}
