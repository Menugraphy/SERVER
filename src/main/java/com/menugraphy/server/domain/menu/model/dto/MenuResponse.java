package com.menugraphy.server.domain.menu.model.dto;

public record MenuResponse(
        Long id,
        String image,
        String name,
        String description,
        int price,
        String localizedPrice,
        boolean isAvoidanceFood
) {

    public static MenuResponse of(
            final Long id,
            final String image,
            final String name,
            final String description,
            final int price,
            final String localizedPrice,
            final boolean isAvoidanceFood
    ) {
        return new MenuResponse(id, image, name, description, price, localizedPrice, isAvoidanceFood);
    }
}
