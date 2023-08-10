package com.flint.flint.common.exception;

import com.flint.flint.common.spec.ResultCode;
import lombok.Getter;
import org.springframework.http.HttpStatus;

/**
 * 클라이언트 에러(4xx)에 대한 플린트 서비스 최상위 커스텀 예외 클래스
 *
 * @author 신승건
 * @since 2023-08-08
 */
@Getter
public class FlintCustomException extends RuntimeException {

    private final HttpStatus httpStatus; // HTTP 상태 코드
    private final ResultCode resultCode; // 커스텀 결과 코드
    private final String resultMessage; // 결과 메세지

    public FlintCustomException(HttpStatus httpStatus, ResultCode resultCode) {
        this(httpStatus, resultCode, resultCode.getMessage());
    }

    public FlintCustomException(HttpStatus httpStatus, ResultCode resultCode, String resultMessage) {
        super(resultMessage);
        this.httpStatus = httpStatus;
        this.resultCode = resultCode;
        this.resultMessage = resultMessage;
    }

}
