package com.alle.api.global.exception;

import lombok.extern.slf4j.Slf4j;
import org.hibernate.exception.ConstraintViolationException;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import static com.alle.api.global.exception.ErrorCode.DUPLICATE_RESOURCE;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler extends ResponseEntityExceptionHandler {

    @ExceptionHandler(value = { ConstraintViolationException.class, DataIntegrityViolationException.class})
    protected ResponseEntity<ExceptionResponse> handleDataException() {
        log.info("handleDataException throw Exception : {}", DUPLICATE_RESOURCE);
        return ExceptionResponse.toResponseEntity(DUPLICATE_RESOURCE);
    }


    @ExceptionHandler(value = { ResourceNotFoundException.class })
    protected ResponseEntity<ExceptionResponse> handleCustomException(ResourceNotFoundException e) {
        log.info("handleCustomException throw CustomException : {}", e.getErrorCode());
        return ExceptionResponse.toResponseEntity(e.getErrorCode());
    }
}
