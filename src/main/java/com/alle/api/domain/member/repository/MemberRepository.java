package com.alle.api.domain.member.repository;


import com.alle.api.domain.member.domain.Member;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface MemberRepository extends JpaRepository<Member, Long> {

    Optional<Member> findByNickName(String nickname);
    Optional<Member> findByEmail(String email);
}
