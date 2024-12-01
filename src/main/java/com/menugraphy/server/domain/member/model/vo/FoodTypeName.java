package com.menugraphy.server.domain.member.model.vo;

public record FoodTypeName(
        String name
) {

    public static FoodTypeName of(String name) {
        return new FoodTypeName(name);
    }
}
