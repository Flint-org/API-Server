package com.flint.flint.club.domain.spec;

import lombok.Getter;

/**
 * Club Join Requirement Type Enum Class : Auth or Auto
 * @author 김기현
 * @since 2023-08-05
 */
@Getter
public enum ClubJoinRequirement {
    AUTH_JOIN("Auth"),
    AUTO_JOIN("Auto");

    private final String type;

    ClubJoinRequirement(String type) {
        this.type = type;
    }
}
