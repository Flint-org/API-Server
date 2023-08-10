package com.flint.flint.common.handler;

import com.flint.flint.common.ResponseForm;
import com.flint.flint.common.exception.FlintCustomException;
import com.flint.flint.common.spec.ResultCode;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

/**
 * 컨트롤러에서 발생하는 전역적인 예외를 캐치해서 핸들링
 *
 * @author 신승건
 * @since 2023-08-08
 */
@RestControllerAdvice
@Slf4j
public class GlobalExceptionHandler {

    /**
     * 예기치 못한 서버 내부의 모든 에러를 핸들링 (상태코드 500 응답) + 에러 로그 내역을 슬랙으로 전송
     *
     * @param e       Java 최상위 예외 클래스
     * @param request 요청 서블릿 객체
     * @return internal server error 에 대한 에러 응답 json
     */
    @ExceptionHandler(Exception.class)
    protected ResponseEntity<ResponseForm<Void>> handleInternalErrorException(Exception e, HttpServletRequest request) {
        log.error("[서버 에러] from {} api", request.getRequestURI(), e);
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(
                new ResponseForm<>(ResultCode.INTERNAL_SERVER_ERROR)
        );
    }

    /**
     * 클라이언트 에러를 커스터마이징하여 핸들링 (상태 코드 4xx 응답)
     *
     * @param e       커스텀 서비스 최상단 예외 클래스
     * @param request 요청 서블릿 객체
     * @return 커스텀 에러 코드의 내용에 따른 에러 응답 json
     */
    @ExceptionHandler(FlintCustomException.class)
    protected ResponseEntity<ResponseForm<Void>> handleCustomClientErrorException(FlintCustomException e, HttpServletRequest request) {
        log.warn("[클라이언트 에러] from {} api", request.getRequestURI(), e);
        return ResponseEntity.status(e.getHttpStatus()).body(
                new ResponseForm<>(e.getResultCode())
        );
    }

}
