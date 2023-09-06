package com.flint.flint.mail.dto.request;

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

    private int authNumber;
    private int admissionYear;
    private String university;
    private String major;
}
