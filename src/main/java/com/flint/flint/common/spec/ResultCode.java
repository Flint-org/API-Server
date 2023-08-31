package com.flint.flint.common.spec;

import lombok.Getter;

/**
 * 커스텀 상태 코드 값 정의
 * 각 도메인 별 비즈니스 로직 안에서 발생한 예외에 대한 상태 코드를 정의하기
 *
 * @author 신승건
 * @since 2023-08-08
 */
@Getter
public enum ResultCode {

    // 정상 처리
    OK("F000", "요청 정상 처리"),

    // 서버 내부 에러 (5xx 에러)
    INTERNAL_SERVER_ERROR("F100", "서버 에러 발생"),

    // F2xx: 인증, 권한에 대한 예외
    MAIL_AUTHNUMBER_NOT("F200", "인증번호가 틀립니다."),
    // F3xx: 유저 예외
    USER_NOT_FOUND("F300", "존재하지 않는 유저입니다."),
    USER_MANY_REQUEST("F301", "사용자의 API요청이 제한됩니다.");
    // F4xx: 커뮤니티, 게시글 예외

    // F5xx: 모임 예외

    private final String code;
    private final String message;

    ResultCode(String code, String message) {
        this.code = code;
        this.message = message;
    }
}
