package com.alle.api.global.security.filter;

import com.alle.api.global.exception.ExceptionCode;
import com.alle.api.global.exception.custom.JwtException;
import com.alle.api.global.security.service.JwtService;
import com.fasterxml.jackson.databind.ObjectMapper;
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
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.Map;

@RequiredArgsConstructor
public class JwtAuthenticationProcessingFilter
        extends OncePerRequestFilter {

    private final JwtService jwtService;

    @Override
    public void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {

        String path = request.getServletPath();
        if ("/api/v1/auth/login".equals(path) || "/api/v1/auth/sign-up".equals(path)) {
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
            handleAuthenticationException(response, ExceptionCode.NOT_FOUND_TOKEN);
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

    private void handleAuthenticationException(HttpServletResponse response, ExceptionCode exceptionCode) throws IOException {
        response.setStatus(exceptionCode.getHttpStatus().value());
        response.setContentType("application/json;charset=UTF-8");

        Map<String, Object> responseBody = new HashMap<>();
        responseBody.put("status", "REJECT");
        responseBody.put("code", exceptionCode.getCode());
        responseBody.put("message", exceptionCode.getMessage());
        responseBody.put("data", null);

        PrintWriter writer = response.getWriter();
        writer.write(new ObjectMapper().writeValueAsString(responseBody));
        writer.flush();
        writer.close();
    }
}
