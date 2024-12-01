package com.menugraphy.server.domain.menu.model.dto;

import jakarta.validation.constraints.Positive;

public record OrderScriptRequest(
        @Positive(message = "메뉴 Id는 양수여야 합니다.")
        Long menuId,
        @Positive(message = "메뉴 수량은 양수여야 합니다.")
        int menuCount
) {

}
