package com.menugraphy.server.domain.menu.model.vo;

public record SimilarFood(
        String image,
        String name
) {

    public static SimilarFood of(
            String image,
            String name
    ) {
        return new SimilarFood(image, name);
    }
}
