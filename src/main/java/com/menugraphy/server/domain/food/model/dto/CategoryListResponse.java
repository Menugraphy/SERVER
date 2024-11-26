package com.menugraphy.server.domain.food.model.dto;

import java.util.List;

public record CategoryListResponse(
        List<CategoryResponse> categoryList
) {

    public static CategoryListResponse of(final List<CategoryResponse> categoryList) {
        return new CategoryListResponse(categoryList);
    }
}
