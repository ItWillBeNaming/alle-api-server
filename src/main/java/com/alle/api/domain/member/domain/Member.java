package com.alle.api.domain.member.domain;

import com.alle.api.domain.board.domain.Board;
import com.alle.api.domain.board.domain.BoardComment;
import com.alle.api.domain.board.domain.Favorite;
import com.alle.api.domain.board.domain.Like;
import com.alle.api.domain.member.constant.Gender;
import com.alle.api.domain.member.constant.MemberStatus;
import com.alle.api.domain.member.constant.RoleType;
import com.alle.api.domain.member.constant.SocialType;
import com.alle.api.domain.promise.domain.PromiseMember;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.UpdateTimestamp;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.time.LocalDate;
import java.util.*;

import java.time.LocalDateTime;

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


    @Column(nullable = false, unique = true)
    private String loginId;

//    @Column(nullable = false)
    private String password;

//    @Column(nullable = false, length = 40)
    private String firstName;

//    @Column(nullable = false, length = 40)
    private String lastName;

//    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private Gender gender;

//    @Column(nullable = false, length = 30, unique = true)
    private String email;


//    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private RoleType role;

    @Enumerated(EnumType.STRING)
    private SocialType socialType;

    private String socialId;

    @Column(name = "refresh_token")
    private String refreshToken;

    @Enumerated(EnumType.STRING)
    private MemberStatus status;

//    @Column(nullable = false, unique = true)
    @Column(name = "nick_name")
    private String nickname;

//    @Column(nullable = false)
    private LocalDate birthDay;

    @Column
    private String profileImageUrl;



    @Column
    private LocalDateTime lastLoginDate;

    @CreatedDate
    private LocalDateTime createdDate;

    @LastModifiedDate
    private LocalDateTime lastModifiedDate;

    @UpdateTimestamp
    private LocalDateTime updateDate;

//    @OneToMany(mappedBy = "member")
//    private List<Board> boards = new ArrayList<>();
//
//    @OneToMany(mappedBy = "member")
//    private List<BoardComment> boardComments = new ArrayList<>();
//
//    @OneToMany(mappedBy = "member")
//    private List<Like> likes = new ArrayList<>();
//
//    @OneToMany(mappedBy = "member")
//    private List<Favorite> favorites = new ArrayList<>();


    @OneToMany(mappedBy = "member")
    private List<PromiseMember> promiseMembers = new ArrayList<>();


    public void authorizeUser(){
        this.role = RoleType.USER;
    }

    public void encodePassword(PasswordEncoder passwordEncoder) {
        this.password = passwordEncoder.encode(this.password);
    }

    public void updateRefreshToken(String updateRefreshToken) {
        this.refreshToken = updateRefreshToken;
    }

}
