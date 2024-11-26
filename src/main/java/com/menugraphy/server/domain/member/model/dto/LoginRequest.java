package com.menugraphy.server.domain.member.model.dto;

import jakarta.validation.constraints.NotBlank;

public record LoginRequest(
        @NotBlank(message = "소셜 로그인 종류는 공백일 수 없습니다.")
        String socialType,
        @NotBlank(message = "idToken은 공백일 수 없습니다.")
        String idToken
) {

}
