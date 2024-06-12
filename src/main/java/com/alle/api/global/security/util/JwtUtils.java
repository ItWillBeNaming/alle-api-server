package com.alle.api.global.security.util;

import com.alle.api.domain.token.repository.RefreshTokenRepository;
import com.alle.api.global.exception.custom.JwtException;
import com.alle.api.global.security.CookieUtils;
import com.alle.api.global.security.service.JwtService;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class JwtUtils {

    private final JwtService jwtService;
    private final RefreshTokenRepository refreshTokenRepository;
    private final CookieUtils cookieUtils;

    public void handleExpiredRefreshToken(String refreshToken, HttpServletResponse response) {

        cookieUtils.deleteCookie(response, "refreshToken");
        jwtService.deleteRefreshTokenDB(refreshToken);

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
