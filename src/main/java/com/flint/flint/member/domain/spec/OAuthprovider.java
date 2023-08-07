package com.flint.flint.member.domain.spec;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @Author 정순원
 * @Since 2023-08-07
 */
@Getter
@AllArgsConstructor
public enum OAuthprovider {

    KAKAO("카카오"),
    NAVER("네이버");

    private String name;
}
