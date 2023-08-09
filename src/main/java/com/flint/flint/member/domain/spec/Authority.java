package com.flint.flint.member.domain.spec;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @Author 정순원
 * @Since 2023-08-07
 */
@AllArgsConstructor
@Getter
public enum Authority {

    USER("ROLE_USER"),
    ADMIN("ROLE_ADMIN");

    private final String role;
}
