package com.alle.api.domain.member.service;

import com.alle.api.domain.member.constant.RoleType;
import com.alle.api.domain.member.domain.Member;
import com.alle.api.domain.member.dto.request.SignInReq;
import com.alle.api.domain.member.dto.request.SignUpReq;
import com.alle.api.domain.member.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class MemberService {

    private final MemberRepository memberRepository;
    private final PasswordEncoder passwordEncoder;

    public void signUp(SignUpReq signUpReq) {

        Member member = Member.builder()
                .loginId(signUpReq.getLoginId())
                .password(signUpReq.getPassword())
                .nickname(signUpReq.getNickname())
                .firstName(signUpReq.getFirstName())
                .lastName(signUpReq.getLastName())
                .role(RoleType.USER)
                .build();

        member.encodePassword(passwordEncoder);
        memberRepository.save(member);

    }


}
