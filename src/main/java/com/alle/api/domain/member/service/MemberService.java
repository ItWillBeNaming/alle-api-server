package com.alle.api.domain.member.service;

import com.alle.api.domain.member.constant.Gender;
import com.alle.api.domain.member.constant.MemberStatus;
import com.alle.api.domain.member.constant.RoleType;
import com.alle.api.domain.member.domain.Member;
import com.alle.api.domain.member.domain.QMember;
import com.alle.api.domain.member.dto.request.SignUpReq;
import com.alle.api.domain.member.dto.request.SocialSignUpReq;
import com.alle.api.domain.member.repository.MemberRepository;
import com.alle.api.global.security.service.JwtService;
import com.querydsl.jpa.impl.JPAQueryFactory;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Optional;

import static com.alle.api.domain.member.domain.QMember.*;

@Service
@RequiredArgsConstructor
@Slf4j

public class MemberService {

    private final MemberRepository memberRepository;
    private final JwtService jwtService;
    private final PasswordEncoder passwordEncoder;
    private final JPAQueryFactory jpaQueryFactory;

    public void signUp(SignUpReq signUpReq) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        String birthDayString = signUpReq.getBirthDay(); // 1994-11-29


        Member member = Member.builder()
                .loginId(signUpReq.getLoginId())
                .password(signUpReq.getPassword())
                .nickname(signUpReq.getNickname())
                .firstName(signUpReq.getFirstName())
                .lastName(signUpReq.getLastName())
                .role(RoleType.USER)
                .email(signUpReq.getEmail())
                .birthDay(LocalDate.parse(birthDayString, formatter))
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


    @Transactional
    public Member signUpSocialMember(SocialSignUpReq socialSignUpReq, HttpServletRequest request) {

        String accessToken = jwtService.extractAccessToken(request);
        log.info("accessToken =" + accessToken);
       String loginId=  jwtService.extractLoginIdFromAccessToken(accessToken);

       log.info("loginId={}",loginId);


        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        String birthDayString = socialSignUpReq.getBirthDay(); // 1994-11-29
        Member findMember = memberRepository.findByLoginId(loginId).get();
        log.info("member={} ", findMember.getSocialId());

        findMember.updateKakaoMember(
                socialSignUpReq.getFirstName(),
                socialSignUpReq.getLastName(),
                socialSignUpReq.getNickname(),
                socialSignUpReq.getEmail(),
                LocalDate.parse(birthDayString,formatter),
                Gender.valueOf(socialSignUpReq.getGender())
        );
        memberRepository.save(findMember);

        return findMember;
    }

@Transactional
    public void deleteMember(long id) {

        jpaQueryFactory.update(member)
                .set(member.status , MemberStatus.D)
                .where(member.id.eq(id))
                .execute();
    }
}


