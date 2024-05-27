package com.alle.api.global.security.service;

import com.alle.api.domain.member.domain.Member;
import com.alle.api.domain.member.repository.MemberRepository;
import com.alle.api.domain.token.entity.RefreshToken;
import com.alle.api.domain.token.repository.RefreshTokenRepository;
import com.alle.api.global.exception.ExceptionCode;
import com.alle.api.global.exception.JwtException;
import com.alle.api.global.exception.MemberException;
import com.alle.api.global.security.CustomUserDetail;
import com.alle.api.global.security.JwtToken;
import io.jsonwebtoken.*;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import java.security.Key;
import java.util.*;
import java.util.stream.Collectors;


@Component
@Slf4j
public class JwtService {


    private final Key key;
    private final long accessTokenExpirationTime;
    private final long refreshTokenExpirationTime;
    private final RefreshTokenRepository refreshTokenRepository;
    private final MemberRepository memberRepository;


    public JwtService(@Value("${jwt.secret}") String secretKey,
                            @Value("${jwt.token.access-token-expiration-time}") long accessTokenExpirationTime,
                            @Value("${jwt.token.refresh-token-expiration-time}") long refreshTokenExpirationTime,
                            RefreshTokenRepository refreshTokenRepository, MemberRepository memberRepository) {
        this.refreshTokenRepository = refreshTokenRepository;
        this.memberRepository = memberRepository;
        byte[] keyBytes = Decoders.BASE64.decode(secretKey);
        this.key = Keys.hmacShaKeyFor(keyBytes);
        this.accessTokenExpirationTime = accessTokenExpirationTime;
        this.refreshTokenExpirationTime = refreshTokenExpirationTime;
    }



    public JwtToken generateToken(Authentication authentication) {
        return JwtToken.builder()
                .grantType("Bearer")
                .accessToken(generateAccessToken(authentication))
                .refreshToken(generateRefreshToken(authentication))
                .build();
    }

    // 리프레시 토큰을 이용해 액세스 토큰을 재발급
    public JwtToken reissueTokenByRefreshToken(String oldRefreshToken) {
        RefreshToken oldRefreshTokenDB = refreshTokenRepository.findByRefreshToken(oldRefreshToken)
                .orElseThrow(() -> new JwtException(ExceptionCode.NOT_FOUND_REFRESH_TOKEN));
        Authentication authentication = getAuthenticationFromMemberId(oldRefreshTokenDB.getMemberId());

        // 새로운 토큰 생성
        String newAccessToken = generateAccessToken(authentication);
        String newRefreshToken = generateRefreshToken(authentication);

        return JwtToken.builder()
                .grantType("Bearer")
                .accessToken(newAccessToken)
                .refreshToken(newRefreshToken)
                .build();
    }

    public Authentication getAuthenticationFromAccessToken(String accessToken) {
        Claims claims = parseClaims(accessToken);

        if (claims.get("auth") == null) {
            throw new JwtException(ExceptionCode.INVALID_TOKEN);
        }

        // 클레임에서 권한 정보 가져오기
        Collection<? extends GrantedAuthority> authorities = Arrays.stream(claims.get("auth").toString().split(","))
                .map(SimpleGrantedAuthority::new)
                .collect(Collectors.toList());

        // UserDetails 객체를 만들어서 Authentication return
        // UserDetails: interface, User: UserDetails를 구현한 class
        // CustomUserDetail 생성!!
        CustomUserDetail userDetail = new CustomUserDetail(claims.getSubject(), "",
                Long.parseLong(String.valueOf(claims.get("id"))), authorities);
        return new UsernamePasswordAuthenticationToken(userDetail, "", authorities);
    }

    public boolean validateToken(String token) {
        try {
            Jwts.parserBuilder()
                    .setSigningKey(key)
                    .build()
                    .parseClaimsJws(token);
            return true;
        } catch (SecurityException | MalformedJwtException e) {
            log.info("Invalid JWT Token", e);
            throw new JwtException(ExceptionCode.INVALID_TOKEN);
        } catch (ExpiredJwtException e) {
            log.info("Expired JWT Token", e);
            throw new JwtException(ExceptionCode.TOKEN_EXPIRED);
        } catch (UnsupportedJwtException e) {
            log.info("Unsupported JWT Token", e);
            throw new JwtException(ExceptionCode.UNSUPPORTED_TOKEN);
        } catch (IllegalArgumentException e) {
            log.info("JWT claims string is empty.", e);
            throw new JwtException(ExceptionCode.NOT_FOUND_TOKEN);
        }
    }

    private String generateAccessToken(Authentication authentication) {
        String authorities = authentication.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.joining(","));

        // CustomUserDetail로 형변환 후 꺼내기
        CustomUserDetail customUserDetail = (CustomUserDetail) authentication.getPrincipal();

        long now = (new Date()).getTime();
        Date accessTokenExpiresIn = new Date(now + accessTokenExpirationTime);

        return Jwts.builder()
                .setSubject(authentication.getName())
                .claim("id", customUserDetail.getId())
                .claim("auth", authorities)
                .setExpiration(accessTokenExpiresIn)
                .signWith(SignatureAlgorithm.HS256, key)
                .compact();
    }

    private String generateRefreshToken(Authentication authentication) {
        CustomUserDetail customUserDetail = (CustomUserDetail) authentication.getPrincipal();
        log.debug("현재 로그인한 사용자의 id: {}", customUserDetail.getId());

        long now = (new Date()).getTime();
        Date refreshTokenExpiresIn = new Date(now + refreshTokenExpirationTime);

        String refreshToken = Jwts.builder()
                .setExpiration(refreshTokenExpiresIn)
                .signWith(SignatureAlgorithm.HS256, key)
                .compact();

        // 사용자 ID로 기존 리프레시 토큰이 있는지 확인
        refreshTokenRepository.findByMemberId(customUserDetail.getId()).ifPresentOrElse(
                existingRefreshToken -> {
                    // 리프레시 토큰이 존재한다면 업데이트
                    existingRefreshToken.updateRefreshToken(refreshToken);
                    refreshTokenRepository.save(existingRefreshToken);
                },
                () -> {
                    // 리프레시 토큰 DB 저장 (새로운 토큰)
                    refreshTokenRepository.save(RefreshToken.of(refreshToken, customUserDetail.getId()));
                }
        );
        return refreshToken;
    }

    private Authentication getAuthenticationFromMemberId(Long memberId) {
        // 회원 ID로 사용자 정보 조회
        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new MemberException(ExceptionCode.NOT_FOUND_MEMBER));
        GrantedAuthority authority = new SimpleGrantedAuthority(member.getRole().name());

        // 권한 정보를 담을 컬렉션 생성
        Collection<GrantedAuthority> authorities = Collections.singletonList(authority);

        // CustomUserDetail 객체 생성
        CustomUserDetail customUserDetail = new CustomUserDetail(member.getEmail(), member.getPassword(),
                member.getId(), authorities);

        // Authentication 객체 생성 및 반환
        return new UsernamePasswordAuthenticationToken(customUserDetail, "", authorities);
    }

    private Claims parseClaims(String accessToken) {
        try {
            return Jwts.parserBuilder()
                    .setSigningKey(key)
                    .build()
                    .parseClaimsJws(accessToken)
                    .getBody();
        } catch (ExpiredJwtException e) {
            return e.getClaims();
        }
    }

}
