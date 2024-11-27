package com.menugraphy.server.domain.menu.model.vo;

public record MenuBoardImage(
        String beforeImage,
        String afterImage
) {

    public static MenuBoardImage of(final String beforeImage, final String afterImage) {
        return new MenuBoardImage(beforeImage, afterImage);
    }
}
