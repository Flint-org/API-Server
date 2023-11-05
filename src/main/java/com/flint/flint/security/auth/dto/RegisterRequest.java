package com.flint.flint.security.auth.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.*;

/**
 * @Author 정순원
 * @Since 2023-08-23
 */
@Getter
@Data
@NoArgsConstructor
@AllArgsConstructor
public class RegisterRequest {

    @NotBlank
    private String providerName;
    @NotBlank
    private String serviceUsingAgree;
    @NotBlank
    private String personalInformationAgree;
    @NotBlank
    private String marketingAgree;
}
