package com.flint.flint.member.domain.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum Gender {

    FEMALE("여자"),
    MALE("남자");

    private String title;

}
