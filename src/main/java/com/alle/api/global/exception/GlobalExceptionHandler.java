package com.alle.api.global.exception;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationServiceException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.security.SignatureException;

import static com.alle.api.global.exception.ExceptionCode.*;

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

    @ExceptionHandler(value = Exception.class)
    public ResponseEntity<ExceptionResponse> handleDefaultException(Exception ex) {
        defaultLogger.error(ex.getMessage(), ex);
        exceptionLogger.error(ex.getMessage(), ex);

        ExceptionResponse exceptionResponse = ExceptionResponse.fromError(ex);
        return ResponseEntity.status(exceptionResponse.httpStatus()).body(exceptionResponse);
    }

    @ExceptionHandler(SignatureException.class)
    public ResponseEntity<ExceptionResponse> handleSignatureException() {
        defaultLogger.warn(INVALID_TOKEN.getMessage());
        ExceptionResponse exceptionResponse = ExceptionResponse.fromException(INVALID_TOKEN);

        return ResponseEntity.status(exceptionResponse.httpStatus()).body(exceptionResponse);
    }

    @ExceptionHandler(AuthenticationServiceException.class)
    public ResponseEntity<ExceptionResponse> handleAuthenticationServiceExcpetion(AuthenticationServiceException e){
        defaultLogger.warn(e.getMessage());
        ExceptionResponse exceptionResponse = ExceptionResponse.fromException(INVALID_TOKEN);

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
