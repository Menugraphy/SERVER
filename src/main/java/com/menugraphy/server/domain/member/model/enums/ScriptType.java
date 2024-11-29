package com.menugraphy.server.domain.member.model.enums;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
public enum ScriptType {

    KOREAN(" 주문할게요.", "이랑 "),
    ROMANIZED(" jumunhalgeyo.", "irang "),
    TRANSLATED(", please.", "and "),
    ;

    private final String suffix;
    private final String separator;
}
