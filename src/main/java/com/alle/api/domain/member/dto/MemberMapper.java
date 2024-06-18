package com.alle.api.domain.member.dto;

import com.alle.api.domain.member.constant.Gender;
import com.alle.api.domain.member.constant.MemberStatus;
import com.alle.api.domain.member.constant.RoleType;
import com.alle.api.domain.member.domain.Member;
import com.alle.api.domain.member.dto.request.SignUpReq;

import java.time.LocalDateTime;

public class MemberMapper {

    public static Member toEntity(String email, String nickName, String profileImg, RoleType role) {

        return Member.builder()
                .email(email)
                .nickName(nickName)
                .password("") // 소셜 회원은 비밀번호 X
                .profileImageUrl(profileImg)
                .role(role)
                .createdDate(LocalDateTime.now())
                .lastModifiedDate(LocalDateTime.now())
                .status(MemberStatus.ACTIVE)
                .build();
    }

    public static Member toEntity(SignUpReq request, String password) {
        return Member.builder()
                .email(request.getEmail())
                .nickName(request.getNickName())
                .password(password)
                .role(RoleType.MEMBER_NORMAL)
                .status(MemberStatus.ACTIVE)
                .firstName(request.getFirstName())
                .lastName(request.getLastName())
                .birthDay(request.getBirthDay())
                .profileImageUrl(null)
                .gender(Gender.valueOf(request.getGender()))
                .build();
    }
}
