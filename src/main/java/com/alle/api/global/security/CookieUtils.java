package com.alle.api.global.security;

import com.alle.api.global.security.service.JwtService;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.util.SerializationUtils;

import java.util.Arrays;
import java.util.Base64;
import java.util.Optional;

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

    public void deleteCookie(HttpServletResponse response, String keyName) {
        Cookie cookie = new Cookie(keyName, null);
        cookie.setMaxAge(0);
        cookie.setPath("/");
        cookie.setHttpOnly(true);
        cookie.setSecure(true);
        response.addCookie(cookie);
    }

    /**
     * jwtService에 있는 refreshToken검증
     *   jwtService.validateToken(refreshToken);
     *
     */
    public String getRefreshToken(HttpServletRequest request) {
        Cookie[] cookies = request.getCookies();

        Optional<String> refreshToken = Arrays.stream(cookies)
                .filter(cookie -> "refreshToken".equals(cookie.getName()))
                .findFirst()
                .map(Cookie::getValue);
        return refreshToken.orElse(null);
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
