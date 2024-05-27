package com.alle.api;

import com.alle.api.global.exception.ExceptionCode;
import com.alle.api.global.exception.JwtException;
import com.alle.api.global.security.filter.JwtAuthenticationProcessingFilter;
import com.alle.api.global.security.service.JwtService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.context.SecurityContextImpl;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

class JwtAuthenticationProcessingFilterTest {

    @Mock
    private JwtService jwtService;

    @Mock
    private FilterChain filterChain;

    @Mock
    private HttpServletRequest request;

    @Mock
    private HttpServletResponse response;

    @InjectMocks
    private JwtAuthenticationProcessingFilter jwtAuthenticationProcessingFilter;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        SecurityContextHolder.setContext(new SecurityContextImpl());
    }

    @Test
    void testDoFilterInternal_ValidToken() throws ServletException, IOException {
        String token = "valid-token";
        when(request.getHeader("Authorization")).thenReturn("Bearer " + token);
        when(request.getMethod()).thenReturn("GET");
        when(jwtService.validateToken(token)).thenReturn(true);
        Authentication authentication = mock(Authentication.class);
        when(jwtService.getAuthenticationFromAccessToken(token)).thenReturn(authentication);

        jwtAuthenticationProcessingFilter.doFilterInternal(request, response, filterChain);

        verify(jwtService).validateToken(token);
        verify(jwtService).getAuthenticationFromAccessToken(token);
        verify(filterChain).doFilter(request, response);
        assertEquals(authentication, SecurityContextHolder.getContext().getAuthentication());
    }

    @Test
    void testDoFilterInternal_InvalidToken() throws ServletException, IOException {
        String token = "invalid-token";
        when(request.getHeader("Authorization")).thenReturn("Bearer " + token);
        when(request.getMethod()).thenReturn("GET");
        when(jwtService.validateToken(token)).thenThrow(new JwtException(ExceptionCode.INVALID_TOKEN));

        jwtAuthenticationProcessingFilter.doFilterInternal(request, response, filterChain);

        verify(jwtService).validateToken(token);
        verify(response).sendError(HttpServletResponse.SC_UNAUTHORIZED, "Invalid token");
    }

    @Test
    void testDoFilterInternal_OptionsMethod() throws ServletException, IOException {
        when(request.getMethod()).thenReturn("OPTIONS");

        jwtAuthenticationProcessingFilter.doFilterInternal(request, response, filterChain);

        verify(filterChain).doFilter(request, response);
    }
}
