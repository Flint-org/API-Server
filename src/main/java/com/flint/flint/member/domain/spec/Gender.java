package com.flint.flint.member.domain.spec;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @Author 정순원
 * @Since 2023-08-07
 */
@Getter
@AllArgsConstructor
public enum Gender {

    FEMALE("여성"),
    MALE("남성");

    private String title;

}
