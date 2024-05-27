package com.alle.api.domain.member.dto.response;

import com.alle.api.domain.member.constant.RoleType;
import com.alle.api.domain.member.domain.Member;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@Builder
public class FindMemberResp {


    private Long id;
    private String email;
    private String nickname;
    private String profileImg;
    private LocalDateTime createdAt;



    public static FindMemberResp of(Member member, String profileImgUrl) {
        return FindMemberResp.builder()
                .id(member.getId())
                .email(member.getEmail())
                .nickname(member.getNickname())
                .profileImg(profileImgUrl)
                .createdAt(member.getCreatedDate())
                .build();
    }
}
