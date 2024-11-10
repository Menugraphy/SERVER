package com.menugraphy.server.domain.food.model.dto;

import java.util.List;

public record CategoryListResponse(
        List<CategoryResponse> categoryList
) {
}
