package com.alle.api.domain.member.repository;


import com.alle.api.domain.member.constant.RoleType;
import com.alle.api.domain.member.domain.Member;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface MemberRepository extends JpaRepository<Member, Long> {

    Optional<Member> findByLoginId(String loginId);

    Optional<Member> findByLoginIdAndRole(String email, RoleType memberRole);

    Optional<Member> findByNickname(String nickname);

    boolean existsByLoginId(String loginId);

    boolean existsByEmail(String s);

    boolean existsByNickname(String s);
}
