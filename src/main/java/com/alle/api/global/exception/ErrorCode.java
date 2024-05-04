package com.alle.api.global.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

import static org.springframework.http.HttpStatus.*;
import static org.springframework.http.HttpStatus.CONFLICT;

@Getter
@AllArgsConstructor
public enum ErrorCode {

    // TODO i18n
    DUPLICATE_RESOURCE(CONFLICT, "error.duplicate.resource"),

    INVALID_PARAMETER(BAD_REQUEST, "잘못된 요청 데이터 입니다."),
    INVALID_AUTHENTICATION(BAD_REQUEST, "잘못된 인증입니다.");

    ;

    private final HttpStatus httpStatus;
    private final String detail;
}
