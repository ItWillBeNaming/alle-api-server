package com.alle.api.domain.member.service;

import com.alle.api.domain.member.domain.Member;
import com.alle.api.domain.member.dto.request.SignUpReq;
import com.alle.api.domain.member.repository.MemberRepository;
import com.alle.api.global.security.service.JwtService;
import com.alle.api.global.security.util.JwtUtils;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.crypto.password.PasswordEncoder;

import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class MemberServiceTest {

    @InjectMocks
    private MemberService memberService;

    @Mock
    private JwtService jwtService;

    @Mock
    private AuthenticationManager authenticationManager;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private MemberRepository memberRepository;

    @Mock
    private JwtUtils jwtUtils;

    @BeforeEach
    void setUp() {
        SignUpReq request = SignUpReq.builder()
                .loginId("test@example.com")
                .password("leesunro12@")
                .passwordConfirm("leesunro12@")
                .nickname("testUser")
                .birthDay("1004-11-22")
                .firstName("Lee")
                .lastName("sunro")
                .gender("MALE")
                .email("sunro@sunr.com")
                .build();

        memberService.join(request);
    }

    @Test
    void join() {
        verify(memberRepository, times(1)).save(any(Member.class));
    }


}