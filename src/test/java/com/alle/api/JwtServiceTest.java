package com.alle.api;

import com.alle.api.domain.member.repository.MemberRepository;
import com.alle.api.domain.token.repository.RefreshTokenRepository;
import com.alle.api.global.exception.JwtException;
import com.alle.api.global.security.CustomUserDetail;
import com.alle.api.global.security.JwtToken;
import com.alle.api.global.security.service.JwtService;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.test.util.ReflectionTestUtils;

import java.security.Key;
import java.util.Collections;
import java.util.Date;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class JwtServiceTest {

    @Mock
    private RefreshTokenRepository refreshTokenRepository;

    @Mock
    private MemberRepository memberRepository;

    private JwtService jwtService;

    private Key key;
    private final long accessTokenExpirationTime = 3600000; // 1 hour
    private final long refreshTokenExpirationTime = 1209600000; // 2 weeks

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        key = Keys.secretKeyFor(SignatureAlgorithm.HS256);
        jwtService = new JwtService("aowieurlwkj12309dfusoirm20394u123sdfasdlfkui2d02kd783",
                accessTokenExpirationTime, refreshTokenExpirationTime, refreshTokenRepository, memberRepository);

        // key 필드에 값을 주입합니다.
        ReflectionTestUtils.setField(jwtService, "key", key);
    }

    @Test
    void testGenerateToken() {
        Authentication authentication = new UsernamePasswordAuthenticationToken(
                new CustomUserDetail("user@example.com", "", 1L, Collections.singletonList(new SimpleGrantedAuthority("ROLE_USER"))),
                "",
                Collections.singletonList(new SimpleGrantedAuthority("ROLE_USER"))
        );

        JwtToken jwtToken = jwtService.generateToken(authentication);
        assertNotNull(jwtToken.getAccessToken());
        assertNotNull(jwtToken.getRefreshToken());
    }

    @Test
    void testValidateToken() {
        String token = Jwts.builder()
                .setSubject("user@example.com")
                .claim("auth", "ROLE_USER")
                .setExpiration(new Date(System.currentTimeMillis() + accessTokenExpirationTime))
                .signWith(key)
                .compact();

            assertTrue(jwtService.validateToken(token));

    }

    @Test
    void testValidateToken_ExpiredToken() {
        String token = Jwts.builder()
                .setSubject("user@example.com")
                .claim("auth", "ROLE_USER")
                .setExpiration(new Date(System.currentTimeMillis() - accessTokenExpirationTime))
                .signWith(key)
                .compact();

        JwtException exception = assertThrows(JwtException.class, () -> jwtService.validateToken(token));
        assertEquals("Token expired", exception.getMessage());
    }

    @Test
    void testGetAuthenticationFromAccessToken() {
        String token = Jwts.builder()
                .setSubject("user@example.com")
                .claim("auth", "ROLE_USER")
                .claim("id", 1L)
                .setExpiration(new Date(System.currentTimeMillis() + accessTokenExpirationTime))
                .signWith(key)
                .compact();

        Authentication authentication = jwtService.getAuthenticationFromAccessToken(token);
        assertNotNull(authentication);
        assertEquals("user@example.com", authentication.getName());
    }
}
