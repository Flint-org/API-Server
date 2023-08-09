package com.flint.flint.club.domain.specification;

import lombok.Getter;

/**
 * Club Frequency Type Enum Class : Once,
 * @author 김기현
 * @since 2023-08-05
 */
@Getter
public enum ClubFrequency {
    REGULAR("Regular"),
    ONCE("Once");

    private final String type;

    ClubFrequency(String type) {
        this.type = type;
    }
}
