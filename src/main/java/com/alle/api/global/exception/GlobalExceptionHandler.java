package com.alle.api.global.exception;

import com.alle.api.global.exception.custom.EmailException;
import com.alle.api.global.exception.custom.JwtException;
import com.alle.api.global.exception.custom.MemberException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

@RestControllerAdvice
public class GlobalExceptionHandler {

    Logger defaultLogger = LoggerFactory.getLogger(GlobalExceptionHandler.class);
    Logger exceptionLogger = LoggerFactory.getLogger("ExceptionLogger");

    @ExceptionHandler(AlleException.class)
    public ResponseEntity<ExceptionResponse> handleAlleException(AlleException ex) {
        defaultLogger.warn(ex.getMessage());
        exceptionLogger.warn(ex.getMessage(), ex);

        ExceptionResponse exceptionResponse = ExceptionResponse.fromException(ex.getExceptionCode());

        return ResponseEntity.status(exceptionResponse.httpStatus()).body(exceptionResponse);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Object> handleValidationExceptions(MethodArgumentNotValidException e) {
        Map<String, Object> responseBody = new LinkedHashMap<>();
        responseBody.put("status", "REJECT");
        responseBody.put("code", 404);
        responseBody.put("message", e.getBindingResult().getFieldErrors().get(0).getDefaultMessage());

        return new ResponseEntity<>(responseBody, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(value = Exception.class)
    public ResponseEntity<ExceptionResponse> handleDefaultException(Exception ex) {
        defaultLogger.error(ex.getMessage(), ex);
        exceptionLogger.error(ex.getMessage(), ex);

        ExceptionResponse exceptionResponse = ExceptionResponse.fromError(ex);
        return ResponseEntity.status(exceptionResponse.httpStatus()).body(exceptionResponse);
    }

    @ExceptionHandler(JwtException.class)
    public ResponseEntity<ExceptionResponse> handleJwtException(JwtException ex) {
        defaultLogger.error(ex.getMessage(), ex);
        exceptionLogger.error(ex.getMessage(), ex);

        ExceptionResponse exceptionResponse = ExceptionResponse.fromError(ex);
        return ResponseEntity.status(exceptionResponse.httpStatus()).body(exceptionResponse);
    }

    @ExceptionHandler(MemberException.class)
    public ResponseEntity<ExceptionResponse> handleMemberException(MemberException ex) {
        defaultLogger.error(ex.getMessage(), ex);
        exceptionLogger.error(ex.getMessage(), ex);
        ExceptionResponse exceptionResponse = ExceptionResponse.fromException(ex.getExceptionCode());
        return ResponseEntity.status(exceptionResponse.httpStatus()).body(exceptionResponse);
    }

    @ExceptionHandler(EmailException.class)
    public ResponseEntity<ExceptionResponse> handleEmailException(EmailException ex) {
        defaultLogger.error(ex.getMessage(), ex);
        exceptionLogger.error(ex.getMessage(), ex);
        ExceptionResponse exceptionResponse = ExceptionResponse.fromError(ex);
        return ResponseEntity.status(exceptionResponse.httpStatus()).body(exceptionResponse);
    }


    /**
     * Security 관련 Exception Handling인데, 버전이 바뀌었나 import가 안된다.
     */
//    @ExceptionHandler(MalformedJwtException.class)
//    public ResponseEntity<ExceptionResponse> handleMalformedJwtException() {
//
//        ExceptionResponse exceptionResponse = ExceptionResponse.from(MALFORMED_TOKEN);
//        return ResponseEntity.status(exceptionResponse.getHttpStatus()).body(exceptionResponse);
//    }
//
//    @ExceptionHandler(ExpiredJwtException.class)
//    public ResponseEntity<ExceptionResponse> handleExpiredJwtException() {
//
//        ExceptionResponse exceptionResponse = ExceptionResponse.from(EXPIRED_TOKEN);
//        return ResponseEntity.status(exceptionResponse.getHttpStatus()).body(exceptionResponse);
//    }
}
