package com.menugraphy.server.domain.food.model.dto;

import java.util.List;

public record CategoryResponse(
        Long id,
        String name,
        List<TypeResponse> typeList
) {
}
