package com.menugraphy.server.domain.menu.model.dto;

public record ImageResponse(
        Long imageId,
        String processedImage
) {

    public static ImageResponse of(final Long imageId, final String processedImage) {
        return new ImageResponse(imageId, processedImage);
    }
}
