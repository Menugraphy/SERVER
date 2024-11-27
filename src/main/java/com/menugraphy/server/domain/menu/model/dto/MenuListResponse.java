package com.menugraphy.server.domain.menu.model.dto;

import java.util.List;

public record MenuListResponse(
        List<MenuResponse> menuList
) {

    public static MenuListResponse of(final List<MenuResponse> menuList) {
        return new MenuListResponse(menuList);
    }
}
