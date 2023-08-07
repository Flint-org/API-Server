package com.flint.flint.member.domain.spec;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @Author 정순원
 * @Since 2023-08-07
 */
@Getter
@AllArgsConstructor
public enum NoticeType {

    YES("이메일수신허용"),
    NO("이메일수신거부");

    private String description;

}
