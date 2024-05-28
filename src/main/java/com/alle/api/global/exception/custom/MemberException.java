package com.alle.api.global.exception.custom;


import com.alle.api.global.exception.AlleException;
import com.alle.api.global.exception.ExceptionCode;

public class MemberException extends AlleException {
    public MemberException(ExceptionCode errorCode) {
        super(errorCode);
    }
}
