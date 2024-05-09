package com.alle.api.global.exception;

import lombok.Getter;
import org.springframework.validation.Errors;
import org.springframework.validation.FieldError;

import java.util.List;

//@Getter
//public class InvalidParameterException extends DefaultException{
//
//    private final Errors errors;
//
//    public InvalidParameterException(Errors errors) {
//        super(ExceptionCode.INVALID_PARAMETER);
//        this.errors = errors;
//    }
//
//    public List<FieldError> getFieldErrors() {
//        return errors.getFieldErrors();
//    }
//
//}
