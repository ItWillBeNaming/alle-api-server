package com.alle.api.domain.member.service;

import com.alle.api.domain.member.constant.RoleType;
import com.alle.api.domain.member.domain.Member;
import com.alle.api.domain.member.dto.request.SignInReq;
import com.alle.api.domain.member.dto.request.SignUpReq;
import com.alle.api.domain.member.dto.response.FindMemberResp;
import com.alle.api.domain.member.repository.MemberRepository;
import com.alle.api.global.exception.ExceptionCode;
import com.alle.api.global.exception.MemberException;
import com.alle.api.global.security.CustomUserDetail;
import com.alle.api.global.security.JwtToken;
import com.alle.api.global.security.service.JwtService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class MemberService {

    private final JwtService jwtService;
    private final AuthenticationManagerBuilder authenticationManagerBuilder;
    private final PasswordEncoder passwordEncoder;
    private final MemberRepository memberRepository;


    public void join(SignUpReq request) {
//TODO:: 유효성 검증 메서드 만들기
//        validateExistingMember(request.getEmail());
//        validateExistingNickname(request.getNickname());
//        validatePassword(request.getPassword(), request.getPasswordConfirm());

        String encodedPassword = passwordEncoder.encode(request.getPassword());
//TODO:: 저장소 만들고 사진 보내기
//        String uploadFileName = determineProfileImgUrl(request.getProfileImage());

        Member member = Member.of(request, encodedPassword);
        memberRepository.save(member);
    }

    public JwtToken login(SignInReq request) {
        Authentication authentication = getUserAuthentication(request);
        return jwtService.generateToken(authentication);
    }

    public FindMemberResp getMemberDetails(Long memberId) {
        Member member = memberRepository.findById(memberId).
                orElseThrow(
                        () -> new MemberException(ExceptionCode.NOT_FOUND_MEMBER)
                );

        String profileImg = member.getProfileImageUrl();

        if (member.getRole().equals(RoleType.MEMBER_NORMAL)) {
            //TODO::사진 업로드
        }
        return FindMemberResp.of(member, profileImg);
    }

    private Authentication getUserAuthentication(SignInReq request) {
        try {
            UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(
                    request.getLoginId(), request.getPassword());
            return authenticationManagerBuilder.getObject().authenticate(authenticationToken);
        } catch (AuthenticationException e) {
            throw new MemberException(ExceptionCode.UNAUTHORIZED_LOGIN);
        }
    }
}
