package com.alle.api.global.security;

import com.alle.api.domain.member.domain.Member;
import lombok.Getter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.oauth2.core.user.OAuth2User;

import java.util.Collection;
import java.util.Map;
import java.util.UUID;

@Getter
public class CustomUserDetail extends User implements OAuth2User {

    private final Long id; // 회원 id
    private Map<String, Object> attributes;

    // 일반 로그인시 사용되는 생성자
    public CustomUserDetail(String username, String password, Long id, Collection<? extends GrantedAuthority> authorities) {
       super(username, password, authorities);
        this.id = id;
    }

    // OAuth2 인증시 사용되는 생성자
    public CustomUserDetail(Member member, Collection<? extends GrantedAuthority> authorities, Map<String, Object> attributes) {
        this(member.getEmail() != null ? member.getEmail() : UUID.randomUUID().toString().substring(0,8)+"@social.com",
                member.getPassword() != null ? member.getPassword() : "",
                member.getId(), authorities);
        this.attributes = attributes;
    }

    @Override
    public Map<String, Object> getAttributes() {
        return attributes;
    }

    @Override
    public String getName() {
        return (String) attributes.get("name");
    }
}
