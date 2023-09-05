package com.flint.flint.mail.dto.request;

import jakarta.persistence.Column;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @Author 정순원
 * @Since 2023-08-31
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class VeriyEmailAuthnumberRequest {

    private int authNumber;
    private String email;
    private int admissionYear;
    private String university;
    private String major;
}
