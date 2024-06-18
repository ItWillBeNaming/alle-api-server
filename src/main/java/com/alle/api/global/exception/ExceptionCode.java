package com.alle.api.global.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

import static org.springframework.http.HttpStatus.*;

@Getter
@AllArgsConstructor
public enum ExceptionCode {

    NOT_HANDLED_EXCEPTION(INTERNAL_SERVER_ERROR, "Error", 900),


    // 400
    PASSWORD_MISMATCH(BAD_REQUEST, "Passwords do not match.", 400),
    INVALID_CURRENT_PASSWORD(BAD_REQUEST, "The current password does not match.", 400),
    INVALID_AUTH_CODE(BAD_REQUEST, "Email authentication failed.", 400),
    NOT_FOUND_REFRESH_TOKEN_IN_COOKIE(BAD_REQUEST, "Refresh token is not found in cookie.", 400),
    INVALID_PARAMETER(BAD_REQUEST, "BAD Request", 400),

    // 401
    FORBIDDEN_SECURITY(FORBIDDEN, "Forbidden", 403),
    UNAUTHORIZED_LOGIN(UNAUTHORIZED, "Login failed: authentication failed.", 401),
    TOKEN_EXPIRED(UNAUTHORIZED, "Expired token.", 401),
    INVALID_TOKEN(UNAUTHORIZED, "Invalid token.", 401),
    UNSUPPORTED_TOKEN(UNAUTHORIZED, "Unsupported token format.", 401),
    NOT_FOUND_TOKEN(UNAUTHORIZED, "Token not found.", 401),
    NOT_FOUND_REFRESH_TOKEN(UNAUTHORIZED, "Refresh token for the user not found.", 401),
    INVALID_AUTHENTICATION(UNAUTHORIZED, "잘못된 인증입니다.", 401),
    MALFORMED_TOKEN(UNAUTHORIZED, "Malformed Token", 401),
    EXPIRED_TOKEN(UNAUTHORIZED, "Expired Token", 401),

    DUPLICATE_RESOURCE(CONFLICT, "error.duplicate.resource", 403),
    NOT_FOUND_MEMBER(CONFLICT,"Can't find Member",403),
    NOT_FOUND_BOARD(CONFLICT,  "Can't find Board" ,403 ),
    MEMBER_ALREADY_EXISTS(CONFLICT,"Member Already Exists" , 403 ),
    NICKNAME_ALREADY_EXISTS(CONFLICT, "Nickname already exists" , 403 ),
    NOT_MATCHED_WRITER(CONFLICT,"Not Matched Writer" ,403 ),
    INVALID_BOARD(CONFLICT, "INVALID BOARD" ,403 ),
    MEMBER_ALREADY_WITHDRAW(CONFLICT, "MEMBER ALREADY WITHDRAW",403 ),
    NOT_FOUND_BOARDCOMMENT(CONFLICT, "Cant' find BoardComment" , 403 );


    private final HttpStatus httpStatus;
    private final String message;
    private final Integer code;
}
