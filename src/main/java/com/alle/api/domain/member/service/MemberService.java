package com.alle.api.domain.member.service;

import com.alle.api.domain.member.constant.Gender;
import com.alle.api.domain.member.constant.MemberStatus;
import com.alle.api.domain.member.constant.RoleType;
import com.alle.api.domain.member.domain.Member;
import com.alle.api.domain.member.dto.request.SignInReq;
import com.alle.api.domain.member.dto.request.SignUpReq;
import com.alle.api.domain.member.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Service
@RequiredArgsConstructor
public class MemberService {

    private final MemberRepository memberRepository;
    private final PasswordEncoder passwordEncoder;

    public void signUp(SignUpReq signUpReq) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy/MM/dd");
        String birthDayString = signUpReq.getBirthDay(); // 1994/11/29


        Member member = Member.builder()
                .loginId(signUpReq.getLoginId())
                .password(signUpReq.getPassword())
                .nickname(signUpReq.getNickname())
                .firstName(signUpReq.getFirstName())
                .lastName(signUpReq.getLastName())
                .role(RoleType.USER)
                .email(signUpReq.getEmail())
                .birthDay(LocalDate.parse(birthDayString,formatter))
                .gender(Gender.valueOf(signUpReq.getGender()))
                .lastModifiedDate(null)
                .lastLoginDate(null)
                .createdDate(LocalDateTime.now())
                .updateDate(null)
                .status(MemberStatus.Y)
                .socialId(null)
                .socialType(null)
                .build();

        member.encodePassword(passwordEncoder);
        memberRepository.save(member);

    }


}
