package com.alle.api.global.exception.custom;

import com.alle.api.global.exception.AlleException;
import com.alle.api.global.exception.ExceptionCode;

public class BoardException extends AlleException {

    public BoardException(ExceptionCode exceptionCode) {
        super(exceptionCode);
    }
}
