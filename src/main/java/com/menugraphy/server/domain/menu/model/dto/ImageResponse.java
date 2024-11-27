package com.menugraphy.server.domain.menu.model.dto;

public record ImageResponse(
        String processedImage
) {

    public static ImageResponse of(final String processedImage) {
        return new ImageResponse(processedImage);
    }
}
