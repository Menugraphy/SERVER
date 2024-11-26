package com.menugraphy.server.domain.food.model.dto;

public record TypeResponse(
        Long id,
        String name
) {

    public static TypeResponse of(
            final Long id,
            final String name
    ) {
        return new TypeResponse(id, name);
    }
}
