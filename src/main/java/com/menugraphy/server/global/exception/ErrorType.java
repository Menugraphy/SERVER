package com.menugraphy.server.global.exception;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public enum ErrorType {

    INVALID_PATH_ERROR(HttpStatus.BAD_REQUEST, "40001", "요청 경로의 변수 값이 허용된 형식과 다릅니다."),
    INVALID_FIELD_ERROR(HttpStatus.BAD_REQUEST, "40002", "요청 본문의 필드 값이 허용된 형식과 다릅니다."),
    NO_REQUEST_PARAMETER_ERROR(HttpStatus.BAD_REQUEST, "40003", "요청에 필요한 파라미터가 존재하지 않습니다."),
    NO_REQUEST_HEADER_ERROR(HttpStatus.BAD_REQUEST, "40004", "요청에 필요한 헤더가 존재하지 않습니다."),
    TYPE_MISMATCH_ERROR(HttpStatus.BAD_REQUEST, "40005", "잘못된 값이 입력되었습니다."),
    INVALID_REQUEST_BODY_ERROR(HttpStatus.BAD_REQUEST, "40006", "잘못된 Request Body입니다. 요청 형식 또는 필드를 확인하세요."),
    DATA_INTEGRITY_VIOLATION_ERROR(HttpStatus.BAD_REQUEST, "40007", "데이터 무결성 제약 조건을 위반했습니다."),
    BEARER_LOST_ERROR(HttpStatus.BAD_REQUEST, "40008", "요청한 토큰이 Bearer 토큰이 아닙니다."),
    INVALID_SOCIAL_TYPE_ERROR(HttpStatus.BAD_REQUEST, "40009", "잘못된 소셜 로그인 종류입니다."),
    EMPTY_PRINCIPAL_ERROR(HttpStatus.BAD_REQUEST, "40010", "Principal 객체가 없습니다. (null)"),
    INVALID_ID_TOKEN_ERROR(HttpStatus.BAD_REQUEST, "40011", "ID 토큰의 서명이 올바르지 않습니다."),
    S3_UPLOAD_ERROR(HttpStatus.BAD_REQUEST, "40012", "S3 이미지 업로드에 실패했습니다."),
    ALREADY_LIKED_FOOD_ERROR(HttpStatus.BAD_REQUEST, "40013", "이미 좋아요를 누른 음식입니다."),
    INVALID_MENU_ID_ERROR(HttpStatus.BAD_REQUEST, "40014", "잘못된 메뉴 ID입니다."),

    UN_LOGIN_ERROR(HttpStatus.UNAUTHORIZED, "40101", "로그인 후 진행해주세요."),

    VALIDATION_ERROR(HttpStatus.UNPROCESSABLE_ENTITY, "42201", "Validation 오류가 발생했습니다."),

    NOT_FOUND_MEMBER_ERROR(HttpStatus.NOT_FOUND, "40401", "존재하지 않는 회원입니다."),
    NOT_FOUND_MENUBOARD_ERROR(HttpStatus.NOT_FOUND, "40402", "존재하지 않는 메뉴판입니다."),
    NOT_FOUND_FOOD_ID_ERROR(HttpStatus.NOT_FOUND, "40404", "존재하지 않는 음식 ID입니다."),
    NOT_FOUND_CATEGORY_ERROR(HttpStatus.NOT_FOUND, "40405", "존재하지 않는 카테고리입니다."),
    NOT_FOUND_TYPE_ERROR(HttpStatus.NOT_FOUND, "40406", "존재하지 않는 타입입니다."),

    INTERNAL_SERVER_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "50001", "예상치 못한 서버 오류가 발생했습니다."),
    FAILED_DOWNLOAD_GOOGLE_PUBLIC_KEY_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "50002", "구글 공개키 다운로드에 실패하였습니다."),
    INTERNAL_FEIGN_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "50003", "FEIGN 에러가 발생했습니다."),

    ;

    private final HttpStatus httpStatus;
    private final String code;
    private final String message;
}
