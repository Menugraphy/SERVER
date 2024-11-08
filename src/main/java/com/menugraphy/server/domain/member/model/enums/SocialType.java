package com.menugraphy.server.domain.member.model.enums;

import com.menugraphy.server.global.exception.CustomException;
import com.menugraphy.server.global.exception.ErrorType;
import java.util.HashMap;
import java.util.Map;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
public enum SocialType {

    GOOGLE("GOOGLE"),
    APPLE("APPLE"),
    ;

    private final String socialType;
    private static final Map<String, SocialType> SOCIAL_TYPE_MAP = new HashMap<>();

    static {
        for (SocialType socialType : SocialType.values()) {
            SOCIAL_TYPE_MAP.put(socialType.getSocialType(), socialType);
        }
    }

    public static SocialType fromValue(String value) {
        SocialType socialType = SOCIAL_TYPE_MAP.get(value);

        if (socialType == null) {
            throw new CustomException(ErrorType.INVALID_SOCIAL_TYPE_ERROR);
        }

        return socialType;
    }
}
