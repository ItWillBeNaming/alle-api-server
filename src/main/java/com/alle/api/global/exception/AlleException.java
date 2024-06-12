package com.alle.api.global.exception;

import org.springframework.http.HttpStatus;

public class AlleException extends RuntimeException {

    private final ExceptionCode exceptionCode;

    public AlleException(ExceptionCode exceptionCode) {
        super(exceptionCode.getMessage());
        this.exceptionCode = exceptionCode;
    }

    public AlleException() {
        super(ExceptionCode.INVALID_PARAMETER.getMessage());
        this.exceptionCode = ExceptionCode.INVALID_PARAMETER;
    }

    public ExceptionCode getExceptionCode() {
        return this.exceptionCode;
    }

    public HttpStatus getHttpStatus() {
        return this.exceptionCode.getHttpStatus();
    }

    public String getErrorMessage() {
        return this.exceptionCode.getMessage();
    }
}
