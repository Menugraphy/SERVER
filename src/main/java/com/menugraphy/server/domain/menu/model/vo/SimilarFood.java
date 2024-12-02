package com.menugraphy.server.domain.menu.model.vo;

public record SimilarFood(
        Long foodId,
        String image,
        String name
) {

    public static SimilarFood of(Long foodId, String image, String name) {
        return new SimilarFood(foodId, image, name);
    }
}
