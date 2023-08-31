package com.flint.flint.mail.dto.request;

import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * @Author 정순원
 * @Since 2023-08-31
 */
@Data
@AllArgsConstructor
public class VeriyEmailAuthnumberRequest {

    private int AuthNumber;
}
