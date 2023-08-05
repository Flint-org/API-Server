package com.flint.flint.member.domain.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum OAuthprovider {

    KAKAO("카카오"),
    NAVER("네이버");

    private String name;
}
