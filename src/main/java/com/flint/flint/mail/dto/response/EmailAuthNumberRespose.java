package com.flint.flint.mail.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * @Author 정순원
 * @Since 2023-08-07
 */
@Data
@AllArgsConstructor
public class EmailAuthNumberRespose {

    private int authNumber;
}
