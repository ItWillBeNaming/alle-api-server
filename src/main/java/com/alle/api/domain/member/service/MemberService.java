package com.alle.api.domain.member.service;

import com.alle.api.domain.member.constant.MemberStatus;
import com.alle.api.domain.member.constant.RoleType;
import com.alle.api.domain.member.domain.Member;
import com.alle.api.domain.member.dto.request.*;
import com.alle.api.domain.member.dto.response.FindMemberResp;
import com.alle.api.domain.member.repository.MemberRepository;
import com.alle.api.domain.token.repository.SocialAccessTokenRepository;
import com.alle.api.global.exception.ExceptionCode;
import com.alle.api.global.exception.custom.MemberException;
import com.alle.api.global.oauth.service.OAuth2RevokeService;
import com.alle.api.global.security.JwtToken;
import com.alle.api.global.security.service.JwtService;
import com.alle.api.global.security.util.JwtUtils;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional
public class MemberService {

    private final JwtService jwtService;
    private final AuthenticationManagerBuilder authenticationManagerBuilder;
    private final PasswordEncoder passwordEncoder;
    private final MemberRepository memberRepository;
    private final JwtUtils jwtUtils;
    private final SocialAccessTokenRepository socialAccessTokenRepository;
    private final OAuth2RevokeService oAuth2RevokeService;


    public void join(SignUpReq request) {
        validateExistingEmail(request.getEmail());
        validateExistingNickname(request.getNickname());
        validatePassword(request.getPassword(), request.getPasswordConfirm());


        String encodedPassword = passwordEncoder.encode(request.getPassword());
//TODO:: 저장소 만들고 사진 보내기
//        String uploadFileName = determineProfileImgUrl(request.getProfileImage());

        Member member = Member.of(request, encodedPassword);
        memberRepository.save(member);
    }

    public JwtToken login(SignInReq request) {
        Authentication authentication = getUserAuthentication(request);
        validateWithdrawMember(request);
        return jwtService.generateToken(authentication);
    }



    public FindMemberResp getMemberDetails(Long memberId) {
        Member member = getMemberById(memberId);

        String profileImg = member.getProfileImageUrl();

        if (member.getRole().equals(RoleType.MEMBER_NORMAL)) {
            //TODO::사진 업로드
        }
        return FindMemberResp.of(member, profileImg);
    }

    private Member getMemberById(Long memberId) {
        return memberRepository.findById(memberId).
                orElseThrow(
                        () -> new MemberException(ExceptionCode.NOT_FOUND_MEMBER)
                );
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


    public void updateMember(UpdateReq request) {
        Member findMember = getMemberById(request.getId());
        validateExistingEmail(request.getEmail());
        validateExistingNickname(request.getNickname());

        findMember.updateInfo(request);

    }

    public void logout(String refreshToken, HttpServletResponse response) {
        jwtUtils.handleExpiredRefreshToken(refreshToken, response);
    }

    public void deleteMember(Long memberId, DeleteRequest request) {
        Member findMember = getMemberById(memberId);
        if (!passwordEncoder.matches(request.getPassword(), findMember.getPassword())) {
            throw new MemberException(ExceptionCode.PASSWORD_MISMATCH);
        }
        memberRepository.deleteById(memberId);
    }


    public void deleteSocialMember(Long id) {
        Member findMember = getMemberById(id);

        socialAccessTokenRepository.findByMember(findMember).ifPresent(
                accessToken -> {
                    String socialAccessToken = accessToken.getSocialAccessToken();
                    revokeSocialAccessToken(findMember,socialAccessToken);
                    socialAccessTokenRepository.delete(accessToken);
                });
        memberRepository.deleteById(id);
    }

    private void revokeSocialAccessToken(Member findMember, String socialAccessToken) {
        switch (findMember.getRole()) {
            case MEMBER_KAKAO -> oAuth2RevokeService.revokeKakao(socialAccessToken);
            case MEMBER_GOOGLE -> oAuth2RevokeService.revokeGoogle(socialAccessToken);
            case MEMBER_NAVER -> oAuth2RevokeService.revokeNaver(socialAccessToken);
        }
    }

    public JwtToken reissueToken(String refreshToken) {
        return jwtService.reissueTokenByRefreshToken(refreshToken);
    }

    public void updatePassword(Long id, UpdatePasswordRequest request) {
        Member findMember = getMemberById(id);

        if (!passwordEncoder.matches(request.getCurrentPassword(), findMember.getPassword())) {
            throw new MemberException(ExceptionCode.INVALID_CURRENT_PASSWORD);
        }

        validatePassword(request.getNewPassword(), request.getNewPasswordConfirm());
        findMember.updatePassword(passwordEncoder.encode(request.getNewPassword()));


    }
    private void validatePassword(String password, String passwordConfirm) {
        if (!password.equals(passwordConfirm)) {
            throw new MemberException(ExceptionCode.PASSWORD_MISMATCH);
        }
    }

    public Object getMemberByLoginIdAndRole(String loginId, RoleType roleType) {
        return memberRepository.findByLoginIdAndRole(loginId,roleType);
    }

    public Member getMemberByEmailAndRole(String email, RoleType role) {
        return memberRepository.findByLoginIdAndRole(email, role)
                .orElseThrow(() -> new MemberException(ExceptionCode.NOT_FOUND_MEMBER));
    }

    private void validateExistingEmail(String email) {
        if (memberRepository.findByLoginIdAndRole(email, RoleType.MEMBER_NORMAL).isPresent()) {
            throw new MemberException(ExceptionCode.MEMBER_ALREADY_EXISTS);
        }

    }

    private void validateExistingNickname(String nickname) {
        if (memberRepository.findByNickname(nickname).isPresent()) {
            throw new MemberException(ExceptionCode.NICKNAME_ALREADY_EXISTS);
        }
    }

    private void validateWithdrawMember(SignInReq request) {
        Member member = memberRepository.findByLoginId(request.getLoginId()).orElseThrow( ()->
                new MemberException(ExceptionCode.NOT_FOUND_MEMBER)
        );
        if (member.getStatus().equals(MemberStatus.D)) {
            throw new MemberException(ExceptionCode.MEMBER_ALREADY_WITHDRAW);
        }
    }

}
