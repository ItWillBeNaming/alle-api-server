package com.alle.api.global.security;

import com.alle.api.global.exception.ExceptionCode;
import com.alle.api.global.exception.custom.JwtException;
import com.alle.api.global.security.service.JwtService;
import com.alle.api.global.security.util.JwtUtils;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.util.SerializationUtils;

import java.util.Base64;

@AllArgsConstructor
@Component
public class CookieUtils {

    private final JwtService jwtService;


    public void addCookie(HttpServletResponse response, String name, String value, int maxAge) {
        Cookie cookie = new Cookie(name, value);
        cookie.setPath("/");
        cookie.setHttpOnly(true);
        cookie.setMaxAge(maxAge);

        response.addCookie(cookie);
    }

    /**
     * jwtService에 있는 refreshToken검증
     *   jwtService.validateToken(refreshToken);
     *
     */
    public  String extractRefreshToken(HttpServletRequest request) {
        Cookie[] cookies = request.getCookies();
        if (cookies != null) {
            for (Cookie cookie : cookies) {
                if (cookie.getName().equals("refreshToken")) {
                    String refreshToken = cookie.getValue();
                    jwtService.validateToken(refreshToken);
                    return refreshToken;
                }
            }
        }
        throw new JwtException(ExceptionCode.NOT_FOUND_REFRESH_TOKEN_IN_COOKIE);
    }

    public static String serialize(Object obj) {
        return Base64.getUrlEncoder()
                .encodeToString(SerializationUtils.serialize(obj));
    }

    public static <T> T deserialize(Cookie cookie, Class<T> cls) {
        return cls.cast(
                SerializationUtils.deserialize(
                        Base64.getUrlDecoder().decode(cookie.getValue())
                )
        );
    }
}
