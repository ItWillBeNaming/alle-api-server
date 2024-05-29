package com.alle.api;

import com.alle.api.global.exception.ExceptionCode;
import com.alle.api.global.exception.custom.JwtException;
import com.alle.api.global.security.filter.JwtAuthenticationProcessingFilter;
import com.alle.api.global.security.service.JwtService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

public class JwtAuthenticationProcessingFilterTest {

    @Mock
    private JwtService jwtService;

    @InjectMocks
    private JwtAuthenticationProcessingFilter jwtAuthenticationProcessingFilter;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testDoFilterInternal_validToken() throws ServletException, IOException {
        // Given
        MockHttpServletRequest request = new MockHttpServletRequest();
        MockHttpServletResponse response = new MockHttpServletResponse();
        FilterChain filterChain = mock(FilterChain.class);

        String token = "Bearer valid-token";
        request.addHeader("Authorization", token);

        Authentication authentication = mock(Authentication.class);
        when(jwtService.validateToken(anyString())).thenReturn(true);
        when(jwtService.getAuthenticationFromAccessToken(anyString())).thenReturn(authentication);

        // When
        jwtAuthenticationProcessingFilter.doFilterInternal(request, response, filterChain);

        // Then
        verify(jwtService, times(1)).validateToken(anyString());
        verify(jwtService, times(1)).getAuthenticationFromAccessToken(anyString());
        assertEquals(authentication, SecurityContextHolder.getContext().getAuthentication());
    }

    @Test
    public void testDoFilterInternal_invalidToken() throws ServletException, IOException {
        // Given
        MockHttpServletRequest request = new MockHttpServletRequest();
        MockHttpServletResponse response = new MockHttpServletResponse();
        FilterChain filterChain = mock(FilterChain.class);

        String token = "Bearer invalid-token";
        request.addHeader("Authorization", token);

        when(jwtService.validateToken(anyString())).thenReturn(false);

        // When
        jwtAuthenticationProcessingFilter.doFilterInternal(request, response, filterChain);

        // Then
        verify(jwtService, times(1)).validateToken(anyString());
        verify(jwtService, times(0)).getAuthenticationFromAccessToken(anyString());
        assertNull(SecurityContextHolder.getContext().getAuthentication());
        assertEquals(HttpServletResponse.SC_UNAUTHORIZED, response.getStatus());
    }

    @Test
    public void testDoFilterInternal_noToken() throws ServletException, IOException {
        // Given
        MockHttpServletRequest request = new MockHttpServletRequest();
        MockHttpServletResponse response = new MockHttpServletResponse();
        FilterChain filterChain = mock(FilterChain.class);

        // When
        jwtAuthenticationProcessingFilter.doFilterInternal(request, response, filterChain);

        // Then
        verify(jwtService, times(0)).validateToken(anyString());
        verify(jwtService, times(0)).getAuthenticationFromAccessToken(anyString());
        assertNull(SecurityContextHolder.getContext().getAuthentication());
    }

    @Test
    public void testDoFilterInternal_tokenValidationException() throws ServletException, IOException {
        // Given
        MockHttpServletRequest request = new MockHttpServletRequest();
        MockHttpServletResponse response = new MockHttpServletResponse();
        FilterChain filterChain = mock(FilterChain.class);

        String token = "Bearer valid-token";
        request.addHeader("Authorization", token);

        when(jwtService.validateToken(anyString())).thenThrow(new JwtException(ExceptionCode.INVALID_TOKEN));

        // When
        jwtAuthenticationProcessingFilter.doFilterInternal(request, response, filterChain);

        // Then
        verify(jwtService, times(1)).validateToken(anyString());
        verify(jwtService, times(0)).getAuthenticationFromAccessToken(anyString());
        assertNull(SecurityContextHolder.getContext().getAuthentication());
        assertEquals(HttpServletResponse.SC_UNAUTHORIZED, response.getStatus());
    }
}
