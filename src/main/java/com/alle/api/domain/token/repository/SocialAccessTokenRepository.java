package com.alle.api.domain.token.repository;


import com.alle.api.domain.member.domain.Member;
import com.alle.api.domain.token.entity.SocialAccessToken;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface SocialAccessTokenRepository extends JpaRepository<SocialAccessToken, Long> {
    Optional<SocialAccessToken> findByMember(Member member);
}
