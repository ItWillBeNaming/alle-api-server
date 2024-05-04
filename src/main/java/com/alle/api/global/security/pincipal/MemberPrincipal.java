package com.alle.api.global.security.pincipal;

import com.alle.api.domain.member.domain.Member;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Map;

public class MemberPrincipal implements UserDetails{

    private Long id;
    private String loginId;
    private String password;
    private Collection<? extends GrantedAuthority> authorities;
    private Map<String, Object> attributes;

    public MemberPrincipal(Long id, String loginId, String password, Collection<? extends GrantedAuthority> authorities) {
        this.id = id;
        this.loginId = loginId;
        this.password = password;
        this.authorities = authorities;
    }

    public static MemberPrincipal create(Member member) {
        List<GrantedAuthority> authorities = Collections.singletonList(new SimpleGrantedAuthority(member.getRole().getValue()));
        return new MemberPrincipal(
                member.getId(),
                member.getLoginId(),
                member.getPassword(),
                authorities
        );
    }

    public static MemberPrincipal create(Member member, Map<String, Object> attributes) {
        MemberPrincipal userPrincipal = MemberPrincipal.create(member);
        userPrincipal.setAttributes(attributes);
        return userPrincipal;
    }

    public void setAttributes(Map<String, Object> attributes) {
        this.attributes = attributes;
    }

    public Long getId() {
        return id;
    }

    public String getLoginId() {
        return loginId;
    }


    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return authorities;
    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public String getUsername() {
        return loginId;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }

}

