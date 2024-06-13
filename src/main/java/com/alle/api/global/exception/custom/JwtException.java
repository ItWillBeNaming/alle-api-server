package com.alle.api.global.exception.custom;


import com.alle.api.global.exception.AlleException;
import com.alle.api.global.exception.ExceptionCode;

public class JwtException extends AlleException {
    public JwtException(ExceptionCode errorCode) {
        super(errorCode);
    }

    public JwtException() {
        super(ExceptionCode.INVALID_PARAMETER);
    }
}
