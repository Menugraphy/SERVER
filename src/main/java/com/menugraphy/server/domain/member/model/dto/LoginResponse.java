package com.menugraphy.server.domain.member.model.dto;

public record LoginResponse(
        String accessToken
) {

    public static LoginResponse of(final String accessToken) {
        return new LoginResponse(accessToken);
    }
}
