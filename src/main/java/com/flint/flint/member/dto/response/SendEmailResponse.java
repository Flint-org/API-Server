package com.flint.flint.member.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * 인증번호 리턴
 * @Author 정순원
 * @Since 2023-08-07
 */
@Data
@AllArgsConstructor
public class SendEmailResponse {

    private int authNumber;
}
