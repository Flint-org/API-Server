package com.flint.flint.member.spec;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @Author 정순원
 * @Since 2023-08-07
 */
@Getter
@AllArgsConstructor
public enum Gender {

    FEMALE("여자"),
    MALE("남자");

    private String title;

}
