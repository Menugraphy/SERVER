package com.menugraphy.server.domain.food.model.dto;

import java.util.List;

public record CategoryResponse(
        Long id,
        String name,
        List<TypeResponse> typeList
) {

    public static CategoryResponse of(
            final Long id,
            final String name,
            final List<TypeResponse> typeList
    ) {
        return new CategoryResponse(id, name, typeList);
    }
}
