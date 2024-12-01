package com.menugraphy.server.domain.menu.model.dto;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Positive;
import java.util.List;

public record OrderScriptListRequest(
        @Positive(message = "메뉴판 Id는 양수여야 합니다.")
        Long menuBoardId,
        List<@Valid OrderScriptRequest> menuOrderList
) {

}
