package com.alle.api.global.exception;

import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.MalformedJwtException;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
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

        ExceptionResponse exceptionResponse = ExceptionResponse.from(ex.getExceptionCode());

        return ResponseEntity.status(exceptionResponse.getHttpStatus()).body(exceptionResponse);
    }

    @ExceptionHandler(value = Exception.class)
    public ResponseEntity<ApiResponse> handleDefaultException(Exception ex) {
        defaultLogger.error(ex.getMessage());
        exceptionLogger.error(ex.getMessage(), ex);

        // TODO
        return ResponseEntity.internalServerError().build();
    }

    @ExceptionHandler(SignatureException.class)
    public ResponseEntity<ExceptionResponse> handleSignatureException() {

        ExceptionResponse exceptionResponse = ExceptionResponse.from(INVALID_TOKEN);

        return ResponseEntity.status(exceptionResponse.getHttpStatus()).body(exceptionResponse);
    }

    @ExceptionHandler(MalformedJwtException.class)
    public ResponseEntity<ExceptionResponse> handleMalformedJwtException() {

        ExceptionResponse exceptionResponse = ExceptionResponse.from(MALFORMED_TOKEN);
        return ResponseEntity.status(exceptionResponse.getHttpStatus()).body(exceptionResponse);
    }

    @ExceptionHandler(ExpiredJwtException.class)
    public ResponseEntity<ExceptionResponse> handleExpiredJwtException() {

        ExceptionResponse exceptionResponse = ExceptionResponse.from(EXPIRED_TOKEN);
        return ResponseEntity.status(exceptionResponse.getHttpStatus()).body(exceptionResponse);
    }

//    @ExceptionHandler(value= InvalidParameterException.class)
//    public ResponseEntity<ExceptionResponse> handleInvalidParameterException(InvalidParameterException e) {
//        return ExceptionResponse.from(INVALID_PARAMETER);
//    }

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
