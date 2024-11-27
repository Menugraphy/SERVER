package com.menugraphy.server.domain.menu.model.dto;

public record OrderScriptResponse(
        String korean,
        String romanized,
        String translatedText
) {

    public static OrderScriptResponse of(
            String korean,
            String romanized,
            String translatedText
    ) {
        return new OrderScriptResponse(korean, romanized, translatedText);
    }
}
