package com.menugraphy.server.domain.menu.model.vo;

public record MenuOrder(
        Long menuId,
        int menuCount
) {

    public static MenuOrder of(Long menuId, int menuCount) {
        return new MenuOrder(menuId, menuCount);
    }
}
