package com.alle.api.global.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

import static org.springframework.http.HttpStatus.*;
import static org.springframework.http.HttpStatus.CONFLICT;

@Getter
@AllArgsConstructor
public enum ExceptionCode {

    NOT_HANDLED_EXCEPTION(INTERNAL_SERVER_ERROR, "Error", 900),

    DUPLICATE_RESOURCE(CONFLICT, "error.duplicate.resource", 1),
    INVALID_PARAMETER(BAD_REQUEST, "BAD Request", 2),
    INVALID_AUTHENTICATION(UNAUTHORIZED, "잘못된 인증입니다.", 3),
    INVALID_TOKEN(UNAUTHORIZED, "Invalid Token", 4),
    MALFORMED_TOKEN(UNAUTHORIZED, "Malformed Token", 5),
    EXPIRED_TOKEN(UNAUTHORIZED, "Expired Token", 6),

    ;

    private final HttpStatus httpStatus;
    private final String message;
    private final Integer code;
}
