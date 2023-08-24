package com.flint.flint.security.auth.dto;

import lombok.Builder;
import lombok.Getter;

/**
 * @Author 정순원
 * @Since 2023-08-23
 */
@Getter
@Builder
public class RegisterRequest {

    private String providerName;
    private String serviceUsingAgree;
    private String personalInformationAgree;
    private String marketingAgree;
}
