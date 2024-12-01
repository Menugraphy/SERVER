package com.menugraphy.server.domain.menu.model.vo;

public record MenuOrderHistory(
        String foodImage,
        String menuName,
        int menuCount
) {

    public static MenuOrderHistory of(String foodImage, String menuName, int menuCount) {
        return new MenuOrderHistory(foodImage, menuName, menuCount);
    }
}
