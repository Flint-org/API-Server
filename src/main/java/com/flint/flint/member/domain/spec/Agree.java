package com.flint.flint.member.domain.spec;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @Author 정순원
 * @Since 2023-08-23
 */
@Getter
@AllArgsConstructor
public enum Agree {

    Y("동의"),
    N("거부");

    private String status;

}
