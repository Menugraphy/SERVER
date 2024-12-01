package com.menugraphy.server.domain.member.model.dto;

import com.menugraphy.server.domain.member.model.vo.FoodTypeName;
import java.util.List;

public record LikedFoodResponse(
        Long id,
        String foodImage,
        String name,
        List<FoodTypeName> foodTypeList
) {

    public static LikedFoodResponse of(Long id, String foodImage, String name, List<FoodTypeName> foodTypeList) {
        return new LikedFoodResponse(id, foodImage, name, foodTypeList);
    }
}
