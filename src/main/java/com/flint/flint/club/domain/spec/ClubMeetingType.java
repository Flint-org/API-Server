package com.flint.flint.club.domain.spec;

import lombok.Getter;

/**
 * Club Meeting Type Enum Class : Online or Offline
 * @author 김기현
 * @since 2023-08-03
 */
@Getter
public enum ClubMeetingType {
    ONLINE("Online"),
    OFFLINE("Offline");

    private final String type;

    ClubMeetingType(String type) {
        this.type = type;
    }
}
