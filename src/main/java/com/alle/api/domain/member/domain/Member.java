package com.alle.api.domain.member.domain;

import com.alle.api.domain.member.constant.Gender;
import com.alle.api.domain.member.constant.MemberStatus;
import com.alle.api.domain.member.constant.RoleType;
import com.alle.api.domain.member.dto.request.SignUpReq;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.UpdateTimestamp;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Collection;
import java.util.Collections;

@Entity
@Table(name = "member")
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
public class Member {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "login_id", nullable = false, unique = true)
    private String loginId;

    private String password;

    private String firstName;

    private String lastName;

    @Enumerated(EnumType.STRING)
    private Gender gender;

    private String email;

    @Enumerated(EnumType.STRING)
    private RoleType role;

    @Enumerated(EnumType.STRING)
    private MemberStatus status;

    private String nickname;

    private LocalDate birthDay;

    private String profileImageUrl;

    private String mapX;

    private String mapY;

    private LocalDateTime lastLoginDate;

    @CreatedDate
    private LocalDateTime createdDate;

    @LastModifiedDate
    private LocalDateTime lastModifiedDate;

    @UpdateTimestamp
    private LocalDateTime updateDate;

    public static Member of(String email, String nickname, String profileImg, RoleType role) {
        return Member.builder()
                .loginId(email)
                .nickname(nickname)
                .password("") // 소셜 회원은 비밀번호 X
                .profileImageUrl(profileImg)
                .role(role)
                .createdDate(LocalDateTime.now())
                .lastModifiedDate(LocalDateTime.now())
                .updateDate(LocalDateTime.now())
                .status(MemberStatus.Y)
                .build();
    }

    public static Member of(SignUpReq request, String password) {
        return Member.builder()
                .loginId(request.getEmail())
                .nickname(request.getNickname())
                .password(password)
                .role(RoleType.MEMBER_NORMAL)
                .status(MemberStatus.Y)
                .email(request.getEmail())
                .firstName(request.getFirstName())
                .lastName(request.getLastName())
                .birthDay(LocalDate.now())
                .profileImageUrl(null)
                .gender(Gender.valueOf(request.getGender()))
                .build();
    }

    public static Member of(SignUpReq request, String password, String profileImg) {
        return Member.builder()
                .email(request.getEmail())
                .nickname(request.getNickname())
                .password(password)
                .profileImageUrl(profileImg)
                .role(RoleType.MEMBER_NORMAL)
                .status(MemberStatus.Y)
                .email(request.getEmail())
                .firstName(request.getFirstName())
                .lastName(request.getLastName())
                .birthDay(LocalDate.now())
                .profileImageUrl(null)
                .gender(Gender.valueOf(request.getGender()))
                .build();
    }

    public void updateProfile(String name, String picture) {
        this.profileImageUrl = picture;
        this.firstName = name;
    }

    // 권한 정보를 반환하는 메서드
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return Collections.singletonList(new SimpleGrantedAuthority(this.role.name()));
    }
}
