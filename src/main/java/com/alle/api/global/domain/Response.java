package com.alle.api.global.domain;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public class Response<T> {

    private int code; // 상태 코드: 200, 400, 404, 500....

    private String status;
    private String message; // 응답 메세지

    @JsonInclude(JsonInclude.Include.NON_NULL)
    private T data;

    // 응답 성공
    public static Response<Void> success(HttpStatus code) {
        return new Response<>(code.value(), "SUCCESS", null, null);
    }

    // 응답 성공 (응답 데이터가 없는 경우)
    public static Response<Void> success(HttpStatus code, String message) {
        return new Response<>(code.value(), "SUCCESS", message, null);
    }

    // 응답 성공 (응답 데이터가 있는 경우)
    public static <T> Response<T> success(HttpStatus code, String message, T data) {
        return new Response<>(code.value(), "SUCCESS", message, data);
    }

    // 응답 성공 (응답 데이터만)
    public static <T> Response<T> success(HttpStatus code, T data) {
        return new Response<>(code.value(), "SUCCESS", null, data);
    }

    // 응답 실패
    public static Response<Void> error(HttpStatus code, String message) {
        return new Response<>(code.value(), "ERROR", message, null);
    }

    public static<T> Response<T> error(HttpStatus code, String message, T data) {
        return new Response<>(code.value(), "ERROR", message, data);
    }

}
