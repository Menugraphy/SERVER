package com.menugraphy.server.global.exception;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public enum ErrorType {

    INVALID_REQUEST_ERROR(HttpStatus.BAD_REQUEST, "40001", "잘못된 요청입니다."),
    TYPE_MISMATCH_ERROR(HttpStatus.BAD_REQUEST, "40002", "잘못된 값이 입력되었습니다."),
    INVALID_REQUEST_BODY_ERROR(HttpStatus.BAD_REQUEST, "40003", "잘못된 Request Body입니다. 요청 형식 또는 필드를 확인하세요."),
    DATA_INTEGRITY_VIOLATION_ERROR(HttpStatus.BAD_REQUEST, "40004", "데이터 무결성 제약 조건을 위반했습니다."),
    BEARER_LOST_ERROR(HttpStatus.BAD_REQUEST, "40005", "요청한 토큰이 Bearer 토큰이 아닙니다."),
    INVALID_SOCIAL_TYPE_ERROR(HttpStatus.BAD_REQUEST, "40006", "잘못된 소셜 로그인 종류입니다."),
    EMPTY_PRINCIPAL_ERROR(HttpStatus.BAD_REQUEST, "40007", "Principal 객체가 없습니다. (null)"),
    INVALID_ID_TOKEN_ERROR(HttpStatus.BAD_REQUEST, "40008", "ID 토큰의 서명이 올바르지 않습니다."),

    EXPIRED_AUTHENTICATION_CODE(HttpStatus.UNAUTHORIZED, "40101", "인가 코드가 만료되었습니다."),
    UN_LOGIN_ERROR(HttpStatus.UNAUTHORIZED, "40102", "로그인 후 진행해주세요."),

    NOT_FOUND_MEMBER_ERROR(HttpStatus.NOT_FOUND, "40401", "존재하지 않는 회원입니다."),

    INTERNAL_SERVER_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "50001", "예상치 못한 서버 오류가 발생했습니다."),
    FAILED_DOWNLOAD_GOOGLE_PUBLIC_KEY_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "50002", "구글 공개키 다운로드에 실패하였습니다."),

    ;

    private final HttpStatus httpStatus;
    private final String code;
    private final String message;
}
