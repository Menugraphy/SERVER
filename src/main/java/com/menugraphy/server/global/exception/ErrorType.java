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
    INVALID_SOCIAL_TYPE_ERROR(HttpStatus.BAD_REQUEST, "40005", "잘못된 소셜 로그인 종류입니다."),

    INTERNAL_SERVER_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "50001", "예상치 못한 서버 오류가 발생했습니다."),

    ;

    private final HttpStatus httpStatus;
    private final String code;
    private final String message;
}
