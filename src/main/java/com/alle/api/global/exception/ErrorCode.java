package com.alle.api.global.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

import static org.springframework.http.HttpStatus.CONFLICT;

@Getter
@AllArgsConstructor
public enum ErrorCode {

    // TODO i18n
    DUPLICATE_RESOURCE(CONFLICT, "error.duplicate.resource"),
    ;

    private final HttpStatus httpStatus;
    private final String detail;
}
