package com.menugraphy.server.global.handler;

import com.menugraphy.server.global.dto.ResponseDto;
import com.menugraphy.server.global.exception.CustomException;
import com.menugraphy.server.global.exception.ErrorType;
import jakarta.validation.ConstraintViolationException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingRequestHeaderException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

    // @Validated 유효성 검사 시 예외 처리
    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<ResponseDto<?>> handleConstraintViolationException(ConstraintViolationException e) {
        return ResponseEntity
                .status(ErrorType.INVALID_PATH_ERROR.getHttpStatus())
                .body(ResponseDto.fail(ErrorType.INVALID_PATH_ERROR, e.getConstraintViolations()));
    }

    // @Valid 유효성 검사 시 예외 처리
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ResponseDto<?>> handleMethodArgumentNotValidException(MethodArgumentNotValidException e) {
        return ResponseEntity
                .status(ErrorType.INVALID_FIELD_ERROR.getHttpStatus())
                .body(ResponseDto.fail(ErrorType.INVALID_FIELD_ERROR, e.getBindingResult()));
    }

    // 필수 요청 파라미터(@RequestParam)가 요청에서 누락됐을 시 예외 처리
    @ExceptionHandler(MissingServletRequestParameterException.class)
    public ResponseEntity<ResponseDto<?>> handleMissingServletRequestParameterException(
            MissingServletRequestParameterException e
    ) {
        return ResponseEntity
                .status(ErrorType.NO_REQUEST_PARAMETER_ERROR.getHttpStatus())
                .body(ResponseDto.fail(ErrorType.NO_REQUEST_PARAMETER_ERROR, e.getParameterName()));
    }

    // 필수 요청 헤더(@RequestHeader)가 요청에서 누락됐을 시 예외 처리
    @ExceptionHandler(MissingRequestHeaderException.class)
    public ResponseEntity<ResponseDto<?>> handleMissingRequestHeaderException(MissingRequestHeaderException e) {
        return ResponseEntity
                .status(ErrorType.NO_REQUEST_HEADER_ERROR.getHttpStatus())
                .body(ResponseDto.fail(ErrorType.NO_REQUEST_HEADER_ERROR, e.getHeaderName()));
    }

    // 컨트롤러 메서드에 전달된 값의 타입 변환 시 예외 처리
    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    public ResponseEntity<ResponseDto<?>> handleTypeMismatchException(MethodArgumentTypeMismatchException e) {
        String paramName = e.getParameter().getParameterName();
        String errorDetail = e.getRequiredType() != null
                ? String.format("'%s'은(는) %s 타입이어야 합니다.", paramName, e.getRequiredType().getSimpleName())
                : String.format("'%s'에 대한 요청 타입이 잘못되었습니다.", paramName);

        return ResponseEntity
                .status(ErrorType.TYPE_MISMATCH_ERROR.getHttpStatus())
                .body(ResponseDto.fail(ErrorType.TYPE_MISMATCH_ERROR, errorDetail));
    }

    // 잘못된 Request Body로 인해 발생하는 예외 처리
    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<ResponseDto<?>> handleHttpMessageNotReadableException(HttpMessageNotReadableException e) {
        return ResponseEntity
                .status(ErrorType.INVALID_REQUEST_BODY_ERROR.getHttpStatus())
                .body(ResponseDto.fail(ErrorType.INVALID_REQUEST_BODY_ERROR));
    }

    // 데이터 무결성 위반 시 예외 처리
    @ExceptionHandler(DataIntegrityViolationException.class)
    public ResponseEntity<ResponseDto<?>> handleDataIntegrityViolationException(DataIntegrityViolationException e) {
        return ResponseEntity
                .status(ErrorType.DATA_INTEGRITY_VIOLATION_ERROR.getHttpStatus())
                .body(ResponseDto.fail(ErrorType.DATA_INTEGRITY_VIOLATION_ERROR));
    }

    // 비즈니스 로직에서 발생하는 예외 처리
    @ExceptionHandler(CustomException.class)
    public ResponseEntity<ResponseDto<?>> handleCustomException(CustomException e) {
        return ResponseEntity
                .status(e.getErrorType().getHttpStatus())
                .body(ResponseDto.fail(e.getErrorType()));
    }

    // 기타 에러 발생 시 예외 처리
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ResponseDto<?>> handleGeneralException(Exception e) {
        log.error("알 수 없는 예외 발생: {}", e.getClass().getName());
        log.error("에러 메시지: {}", e.getMessage());

        return ResponseEntity
                .status(ErrorType.INTERNAL_SERVER_ERROR.getHttpStatus())
                .body(ResponseDto.fail(ErrorType.INTERNAL_SERVER_ERROR, e.getMessage()));
    }
}
