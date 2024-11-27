package com.menugraphy.server.domain.menu.model.dto;

import com.menugraphy.server.domain.menu.model.vo.SimilarFood;
import com.menugraphy.server.domain.menu.model.vo.TypeName;
import java.util.List;

public record MenuDetailResponse(
        Long foodId,
        String image,
        List<TypeName> foodTypeList,
        String name,
        String description,
        List<SimilarFood> similarFoodList
) {

    public static MenuDetailResponse of(
            final Long foodId,
            final String image,
            final List<TypeName> foodTypeList,
            final String name,
            final String description,
            final List<SimilarFood> similarFoodList
    ) {
        return new MenuDetailResponse(foodId, image, foodTypeList, name, description, similarFoodList);
    }
}
