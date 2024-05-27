package com.alle.api.domain.token.entity;

import com.alle.api.domain.member.domain.Member;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class SocialAccessToken {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String socialAccessToken;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Member member;

    @Builder
    private SocialAccessToken(String socialAccessToken, Member member) {
        this.socialAccessToken = socialAccessToken;
        this.member = member;
    }

    public static SocialAccessToken of(String socialAccessToken, Member member) {
        return SocialAccessToken.builder()
                .socialAccessToken(socialAccessToken)
                .member(member)
                .build();
    }


    public void updateSocialAccessToken(String socialAccessToken) {
        this.socialAccessToken = socialAccessToken;
    }
}
