package com.alle.api.global.security.filter;

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

    private final JwtService jwtSer;

    @Override
    public void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {

        String token = resolveToken(request);

        try {
            if (token != null && jwtSer.validateToken(token)) {
                Authentication authentication = jwtSer.getAuthenticationFromAccessToken(token);
                SecurityContextHolder.getContext().setAuthentication(authentication);
            }
        } catch (JwtException e) {
            response.sendError(HttpServletResponse.SC_UNAUTHORIZED, e.getMessage());
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
