package com.flint.flint.member.domain.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum NoticeType {

    YES("이메일수신허용"),
    NO("이메일수신거부");

    private String description;

}
