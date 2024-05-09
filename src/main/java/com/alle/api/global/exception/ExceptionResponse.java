package com.alle.api.global.exception;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import org.springframework.http.HttpStatus;

@JsonInclude(JsonInclude.Include.NON_NULL)
public record ExceptionResponse(HttpStatus httpStatus, String status, Integer code, String message) {

    public static ExceptionResponse from(ExceptionCode errorCode) {
        return new ExceptionResponse(errorCode.getHttpStatus(), "REJECT", errorCode.getCode(), errorCode.getMessage());
    }
    @JsonIgnore
    public HttpStatus getHttpStatus() {
        return httpStatus;
    }

}