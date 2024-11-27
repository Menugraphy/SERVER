package com.menugraphy.server.domain.menu.model.vo;

public record TypeName(
        String typeName
) {

    public static TypeName of(String typeName) {
        return new TypeName(typeName);
    }
}
