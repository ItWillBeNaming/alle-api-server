package com.alle.api.global.security.filter;

import com.alle.api.global.exception.ExceptionCode;
import com.alle.api.global.exception.custom.JwtException;
import com.alle.api.global.security.service.JwtService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@RequiredArgsConstructor
public class JwtAuthenticationProcessingFilter
        extends OncePerRequestFilter {

    private final JwtService jwtService;

    @Override
    public void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {

        String path = request.getServletPath();
        if ("/api/v1/login".equals(path) || "/api/v1/sign-up".equals(path)) {
            filterChain.doFilter(request, response);
            return;
        }

        String token = resolveToken(request);

        try {
            if (token != null && jwtService.validateToken(token)) {
                Authentication authentication = jwtService.getAuthenticationFromAccessToken(token);
                SecurityContextHolder.getContext().setAuthentication(authentication);
            }
        } catch (JwtException e) {
            throw new JwtException(ExceptionCode.NOT_FOUND_TOKEN);
        }
        filterChain.doFilter(request, response);
    }

    private String resolveToken(HttpServletRequest request) {
        String bearerToken = request.getHeader("Authorization");
        if (StringUtils.hasText(bearerToken) && bearerToken.startsWith("Bearer")) {
            return bearerToken.substring(7);
        }
        return null;
    }
}
