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
    USER_MANY_REQUEST("F301", "사용자의 API요청이 제한됩니다."),
    // F4xx: 커뮤니티, 게시글 예외
    MAJOR_BOARD_NOT_FOUND("F400", "존재하지 않는 전공 게시판입니다."),

    // F5xx: 모임 예외
    CLUB_NOT_FOUND_ERROR("F500", "모임을 찾을 수 없습니다."),
    CLUB_NOT_MATCH_CATEGORY_ERROR("F501", "카테고리에 맞지 않는 모임입니다."),
    CLUB_CATEGORY_NOT_FOUND_ERROR("F502", "모임에 맞는 카테고리를 찾을 수 없습니다."),
    CLUB_ENVIRONMENT_NOT_FOUND_ERROR("F503", "모임에 맞는 환경 설정을 찾을 수 없습니다."),
    CLUB_REQUIREMENT_NOT_FOUND_ERROR("F504", "모임에 맞는 요구조건을 찾을 수 없습니다.");

    private final String code;
    private final String message;

    ResultCode(String code, String message) {
        this.code = code;
        this.message = message;
    }
}
