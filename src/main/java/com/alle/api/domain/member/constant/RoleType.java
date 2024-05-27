package com.alle.api.domain.member.constant;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum RoleType {

    MEMBER_NORMAL,
    MEMBER_GOOGLE,
    MEMBER_KAKAO,
    MEMBER_NAVER,
    ADMIN;

    public static RoleType of(String provider) {
        return switch (provider.toLowerCase()) {
            case "google" -> MEMBER_GOOGLE;
            case "kakao" -> MEMBER_KAKAO;
            case "naver" -> MEMBER_NAVER;
            default -> MEMBER_NORMAL;
        };
    }
}
