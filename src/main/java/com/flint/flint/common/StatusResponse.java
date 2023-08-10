package com.flint.flint.common;

import com.flint.flint.common.spec.ResultCode;
import lombok.Getter;

/**
 * 요청 결과의 상태를 저장하는 클래스
 * @author 신승건
 * @since 2023-08-08
 */
@Getter
public class StatusResponse {
    private final String resultCode;
    private final String resultMessage;

    public StatusResponse(ResultCode resultCode) {
        this(resultCode.getCode(), resultCode.getMessage());
    }

    public StatusResponse(String resultCode, String resultMessage) {
        this.resultCode = resultCode;
        this.resultMessage = resultMessage;
    }
}
