package com.menugraphy.server.domain.menu.model.dto;

import java.util.List;

public record OrderScriptListRequest(
        List<OrderScriptRequest> menuOrderList
) {

}
