package com.alle.api.global.exception.custom;

import com.alle.api.global.exception.AlleException;
import com.alle.api.global.exception.ExceptionCode;

public class EmailException extends AlleException {
    public EmailException(ExceptionCode exceptionCode) {
        super(exceptionCode);
    }
}
