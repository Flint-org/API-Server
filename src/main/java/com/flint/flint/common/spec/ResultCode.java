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
    AUTH_USER_NOT("F201", "현재 권한으로 접근 불가능합니다."),
    JWT_DATE_NOT("F202", "JWT토큰이 만료되었습니다."),

    // F3xx: 유저 예외
    USER_NOT_FOUND("F300", "존재하지 않는 유저입니다."),
    USER_MANY_REQUEST("F301", "사용자의 API요청이 제한됩니다."),
    USER_UNIVERSITY_CERTIFICATION_NOT_FOUND("F302", "유저의 대학 정보가 없습니다"),
    USER_ALREADY_JOIN("F303", "이미 회원가입된 유저입니다."),
    USER_NOT_JOINED("F304", "회원가입이 되어있지 않은 유저입니다."),

    // F4xx: 커뮤니티, 게시글 예외
    MAJOR_BOARD_NOT_FOUND("F400", "존재하지 않는 전공 게시판입니다."),
    BOARD_NOT_FOUND("F401", "존재하지 않는 게시판입니다."),
    EXCESS_POST_IMAGE_LIMIT("F402", "게시글의 최대 이미지 개수는 20장입니다."),
    ALREADY_BOOKMARKED_BOARD("F403", "이미 즐겨찾기된 게시판입니다."),
    UNKNOWN_BOOKMARK_BOARD("F404", "해당 게시판은 즐겨찾기가 되어있지 않습니다."),

    POST_NOT_FOUND("405", "존재하지 않는 게시글입니다."),
    POST_COMMENT_NOT_FOUND("406", "존재하지 않는 게시글 댓글입니다."),
    POST_COMMENT_NOT_WRITER("407", "글쓴이가 아닙니다."),
    MAJOR_UPPER_BOARD_NOT_FOUND("F408", "존재하지 않는 전공 대분류입니다."),
    POST_SCRAP_NOT_FOUND("409", "존재하지 않는 게시물 스크랩입니다."),
    POST_COMMENT_LIKE_NOT_FOUND("410", "존재하지 않는 게시물 댓글 좋아요입니다."),
    POST_LIKE_NOT_FOUND("411", "존재하지 않는 게시물 좋아요입니다."),
    USER_ALREADY_REPORTED("412", "이미 게시글을 신고하였습니다."),

    // F5xx: 모임 예외
    CLUB_NOT_FOUND_ERROR("F500", "모임을 찾을 수 없습니다."),
    CLUB_NOT_MATCH_CATEGORY_ERROR("F501", "카테고리에 맞지 않는 모임입니다."),
    CLUB_CATEGORY_NOT_FOUND_ERROR("F502", "모임에 맞는 카테고리를 찾을 수 없습니다."),
    CLUB_ENVIRONMENT_NOT_FOUND_ERROR("F503", "모임에 맞는 환경 설정을 찾을 수 없습니다."),
    CLUB_REQUIREMENT_NOT_FOUND_ERROR("F504", "모임에 맞는 요구조건을 찾을 수 없습니다."),
    CLUB_COMMENT_NOT_FOUND_ERROR("F505", "모임 댓글을 찾을 수 없습니다."),
    CLUB_COMMENT_PATTERN_NOT_MATCH("F506", "모임 댓글의 형식이 올바르지 않습니다."),
    CLUB_COMMENT_USER_NOT_FOUND("F507", "모임 댓글을 작성한 유저를 찾을 수 없습니다"),

    // F6xx: 미디어 예외
    AWS_CREDENTIAL_FAIL("F600", "S3 접근을 위한 AWS 자격 증명에 실패했습니다."),
    EMPTY_MEDIA_FILES("F601", "미디어 파일 저장 요청에서 파일이 비어있습니다."),
    IMPOSSIBLE_EXTRACT_ORIGINAL_FILE_NAME("F602", "파일 이름을 추출할 수 없습니다."),
    UNKNOWN_BUCKET_NAME("F603", "버킷 이름 식별할 수 없습니다."),
    INVALID_IMAGE_EXTENSION_TYPE("F604", "이미지 파일의 확장자가 아닙니다."),
    MEDIA_FILE_SAVE_ERROR("F605", "미디어 파일을 저장하는데 문제가 발생했습니다."),
    MEDIA_FILE_DELETE_ERROR("F606", "미디어 파일을 삭제하는데 문제가 발생했습니다."),
    UNEXPECTED_AWS_SERVICE_ERROR("F607", "AWS 서비스를 이용하는데 예상치 못한 문제가 발생했습니다."),
    NOT_EXIST_OBJECT_GIVEN_PATH("F608", "주어진 경로에 파일이 존재하지 않습니다"),

    // F7xx: asset 예외
    NOT_FOUND_UNIVERSITY_NAME("F700", "존재하지 않는 대학 이름입니다."),
    EMPTY_MAJOR_SEARCH("F701", "검색 결과가 없습니다."),


    // F8xx: JSon 값 예외
    NOT_VALIDATION("F800", "json 값이 올바르지 않습니다."),

    // F9xx: 명함 예외
    IDCARD_NOT_FOUND("F900", "명함을 찾을 수 없습니다."),
    IDCARDBOX_NOT_FOUND("F901", "유저가 이 명함을 가지고 있지 않습니다.");

    private final String code;
    private final String message;


    ResultCode(String code, String message) {
        this.code = code;
        this.message = message;
    }
}
