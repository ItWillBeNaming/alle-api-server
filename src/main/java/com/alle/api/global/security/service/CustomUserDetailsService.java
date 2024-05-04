package com.alle.api.global.security.service;
import com.alle.api.domain.member.domain.Member;
import com.alle.api.domain.member.repository.MemberRepository;
import com.alle.api.global.DefaultAssert;
import com.alle.api.global.security.pincipal.MemberPrincipal;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Optional;


@RequiredArgsConstructor
@Service
public class CustomUserDetailsService implements UserDetailsService{

    private final MemberRepository memberRepository;

    @Override
    public UserDetails loadUserByUsername(String loginId) throws UsernameNotFoundException {

        Optional<Member> member = memberRepository.findByLoginId(loginId);
        DefaultAssert.isOptionalPresent(member);


        return MemberPrincipal.create(member.get());
    }

    @Transactional
    public UserDetails loadUserById(Long id) {
        Optional<Member> member = memberRepository.findById(id);
        DefaultAssert.isOptionalPresent(member);

        return MemberPrincipal.create(member.get());
    }

}
