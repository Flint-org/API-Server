package com.flint.flint.club.domain.spec;

import lombok.Getter;

@Getter
public enum MemberJoinStatus {
    WAIT("Wait"),
    SUCCESS("Success");

    private final String type;

    MemberJoinStatus(String type) {
        this.type = type;
    }
}
