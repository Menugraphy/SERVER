package com.menugraphy.server.domain.member.model.dto;

import jakarta.validation.constraints.Positive;

public record AvoidedTypeRequest(
        @Positive(message = "categoryId 양수여야 합니다.")
        Long categoryId,
        @Positive(message = "typeId 양수여야 합니다.")
        Long typeId
) {

}
