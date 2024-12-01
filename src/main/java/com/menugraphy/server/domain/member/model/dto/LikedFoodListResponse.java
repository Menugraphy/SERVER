package com.menugraphy.server.domain.member.model.dto;

import java.util.List;

public record LikedFoodListResponse(
        List<LikedFoodResponse> likedFoodList
) {

    public static LikedFoodListResponse of(List<LikedFoodResponse> likedFoodList) {
        return new LikedFoodListResponse(likedFoodList);
    }
}
