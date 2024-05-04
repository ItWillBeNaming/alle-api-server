package com.alle.api.domain.auth.service;

import com.alle.api.domain.auth.domain.Token;
import com.alle.api.domain.auth.dto.TokenMapping;
import com.alle.api.domain.auth.dto.request.SignInReq;
import com.alle.api.domain.auth.dto.request.SignUpReq;
import com.alle.api.domain.auth.dto.response.AuthRes;
import com.alle.api.domain.auth.repository.TokenRepository;
import com.alle.api.domain.member.constant.MemberStatus;
import com.alle.api.domain.member.constant.RoleType;
import com.alle.api.domain.member.domain.Member;
import com.alle.api.domain.member.repository.MemberRepository;
import com.alle.api.global.DefaultAssert;
import com.alle.api.global.payload.Message;
import com.alle.api.global.security.service.CustomTokenProviderService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.time.LocalDateTime;

@RequiredArgsConstructor
@Transactional(readOnly = true)
@Service
public class AuthService {

    private final AuthenticationManager authenticationManager;
    private final PasswordEncoder passwordEncoder;
    private final CustomTokenProviderService customTokenProviderService;

    private final MemberRepository userRepository;
    private final TokenRepository tokenRepository;

    //회원가입
    @Transactional
    public Message signUp(SignUpReq signUpReq) {
        DefaultAssert.isTrue(!userRepository.existsByLoginId(signUpReq.getLoginId()), "이미 존재하는 이메일입니다.");

        System.out.println(signUpReq.getFirstName());
        Member member = Member.builder()
                .firstName(signUpReq.getFirstName())
                .lastName(signUpReq.getLastName())
                .loginId(signUpReq.getLoginId())
                .password(passwordEncoder.encode(signUpReq.getPassword()))
                .birthDay(signUpReq.getBirthDate())
                .gender(signUpReq.getGender())
                .email(signUpReq.getEmail())
                .createdDate(LocalDateTime.now())
                .profileImageUrl(signUpReq.getProfileImageUrl())
                .nickName(signUpReq.getNickName())
                .role(RoleType.USER)
                .lastLoginDate(LocalDateTime.now())
                .lastModifiedDate(LocalDateTime.now())
                .status(MemberStatus.Y)
                .build();

        userRepository.save(member);

        URI location = ServletUriComponentsBuilder
                .fromCurrentContextPath().path("/auth/")
                .buildAndExpand(member.getId()).toUri();

        return Message.builder().message("회원가입 성공").build();


    }

    //로그인
    @Transactional
    public AuthRes signin(SignInReq signInRequest){
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        signInRequest.getLoginId(),
                        signInRequest.getPassword()
                )
        );

        SecurityContextHolder.getContext().setAuthentication(authentication);

        TokenMapping tokenMapping = customTokenProviderService.createToken(authentication);
        Token token = Token.builder()
                .refreshToken(tokenMapping.getRefreshToken())
                .loginId(tokenMapping.getLoginId())
                .build();
        tokenRepository.save(token);
        return AuthRes.builder().accessToken(tokenMapping.getAccessToken()).refreshToken(token.getRefreshToken()).build();
    }
}
