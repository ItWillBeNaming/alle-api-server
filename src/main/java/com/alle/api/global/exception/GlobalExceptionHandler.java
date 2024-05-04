package com.alle.api.global.exception;

import lombok.extern.slf4j.Slf4j;
import org.hibernate.exception.ConstraintViolationException;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import static com.alle.api.global.exception.ErrorCode.*;

@RestControllerAdvice
public class GlobalExceptionHandler extends ResponseEntityExceptionHandler {

    @ExceptionHandler(value = Exception.class)
    public ResponseEntity<ExceptionResponse> handleDataException() {
        return ExceptionResponse.toResponseEntity(DUPLICATE_RESOURCE);
    }

    @ExceptionHandler(value= InvalidParameterException.class)
    public ResponseEntity<ExceptionResponse> handleInvalidParameterException(InvalidParameterException e) {
        return ExceptionResponse.toResponseEntity(INVALID_PARAMETER);
    }






//    @Override
//    protected ResponseEntity<Object> handleHttpRequestMethodNotSupported(
//            HttpRequestMethodNotSupportedException ex,
//            HttpHeaders headers, HttpStatus status, final WebRequest request) {
//        logger.info("HttpRequestMethodNotSupported : ", ex);
//
//        return ResponseEntity.badRequest().body(new ExceptionResponse("적합한 HTTP Method로 요청해주세요."));
//    }

//    @ExceptionHandler(value = { ResourceNotFoundException.class })
//    protected ResponseEntity<ExceptionResponse> handleCustomException(ResourceNotFoundException e) {
//        log.info("handleCustomException throw CustomException : {}", e.getErrorCode());
//        return ExceptionResponse.toResponseEntity(e.getErrorCode());
//    }
}
