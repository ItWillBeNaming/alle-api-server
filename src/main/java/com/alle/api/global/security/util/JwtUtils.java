package com.alle.api.global.security.util;

import com.alle.api.domain.token.repository.RefreshTokenRepository;
import com.alle.api.global.exception.ExceptionCode;
import com.alle.api.global.exception.custom.JwtException;
import com.alle.api.global.exception.custom.MemberException;
import com.alle.api.global.security.service.JwtService;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class JwtUtils {

    private final JwtService   jwtService;
    private final RefreshTokenRepository refreshTokenRepository;

    public void handleLogout(String refreshToken, HttpServletResponse response) {
        if(refreshToken== null || refreshToken.isEmpty()) {
            throw new MemberException(ExceptionCode.NOT_FOUND_REFRESH_TOKEN_IN_COOKIE);
        }

        refreshTokenRepository.deleteByRefreshToken(refreshToken);
        deleteRefreshTokenInCookie(response);
    }

    private void deleteRefreshTokenInCookie(HttpServletResponse response) {
        Cookie cookie = new Cookie("refreshToken", null);
        cookie.setMaxAge(0);
        cookie.setPath("/");
        cookie.setHttpOnly(true);
        cookie.setSecure(true);
        response.addCookie(cookie);
    }

    private boolean validateRefreshToken(String refreshToken) {
        try {
            jwtService.validateToken(refreshToken);
            return refreshTokenRepository.existsByRefreshToken(refreshToken);
        } catch (JwtException e) {
            return false;
        }
    }
}
