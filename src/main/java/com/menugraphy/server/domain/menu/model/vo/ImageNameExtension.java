package com.menugraphy.server.domain.menu.model.vo;

public record ImageNameExtension(
        String key,
        String extension,
        String beforeImagePath
) {

    public static ImageNameExtension of(String key, String extension, String beforeImagePath) {
        return new ImageNameExtension(key, extension, beforeImagePath);
    }
}
