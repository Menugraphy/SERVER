package com.menugraphy.server.global.external.client.dto;

import com.menugraphy.server.domain.member.model.enums.SocialType;

public record MemberInfoResponse(
        SocialType socialType,
        String socialId
) {

    public static MemberInfoResponse of(
            final SocialType socialType,
            final String socialId
    ) {
        return new MemberInfoResponse(socialType, socialId);
    }
}
