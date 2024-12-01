package com.menugraphy.server.domain.menu.model.vo;

import com.fasterxml.jackson.annotation.JsonInclude;

@JsonInclude(JsonInclude.Include.NON_NULL)
public record ImageNameExtension(
        String key,
        String extension,
        String beforeImagePath,
        Double latitude,
        Double longitude
) {

    public static ImageNameExtension of(String key, String extension, String beforeImagePath,
                                        Double latitude, Double longitude) {
        return new ImageNameExtension(key, extension, beforeImagePath, latitude, longitude);
    }
}
