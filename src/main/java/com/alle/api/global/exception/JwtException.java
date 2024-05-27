package com.alle.api.global.exception;


public class JwtException extends AlleException {
    public JwtException(ExceptionCode errorCode) {
        super(errorCode);
    }
}
