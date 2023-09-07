package com.flint.flint.mail.dto.request;

import jakarta.validation.constraints.NotBlank;
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
public class SuccessUniversityAuthRequest {

    @NotNull
    private int authNumber;
    @NotNull
    private int admissionYear;
    @NotBlank
    private String university;
    @NotBlank
    private String major;
}
