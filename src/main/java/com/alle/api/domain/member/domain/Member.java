package com.alle.api.domain.member.domain;

import com.alle.api.domain.board.domain.Board;
import com.alle.api.domain.board.domain.BoardComment;
import com.alle.api.domain.board.domain.Favorite;
import com.alle.api.domain.board.domain.Like;
import com.alle.api.domain.promise.domain.PromiseMember;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import java.util.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "member")
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Member {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 30, unique = true)
    private String email;

    @Column(nullable = false, unique = true)
    private String loginId;

    @Column(nullable = false)
    private String password;

    @Column(nullable = false, length = 40)
    private String firstName;

    @Column(nullable = false, length = 40)
    private String lastName;

    @Column(nullable = false)
    private String gender;

    @Column(nullable = false, unique = true)
    private String nickName;

    @Column(nullable = false)
    private String birthDay;

    @Column
    private String profileImageUrl;

    @Column
    private LocalDateTime lastLoginDate;

    @CreatedDate
    private LocalDateTime createdDate;

    @LastModifiedDate
    private LocalDateTime lastModifiedDate;

    @OneToMany(mappedBy = "author")
    private List<Board> boards = new ArrayList<>();

    @OneToMany(mappedBy = "member")
    private List<BoardComment> boardComments = new ArrayList<>();

    @OneToMany(mappedBy = "member")
    private List<Like> likes = new ArrayList<>();

    @OneToMany(mappedBy = "member")
    private List<Favorite> favorites = new ArrayList<>();


    @OneToMany(mappedBy = "member")
    private List<PromiseMember> promiseMembers = new ArrayList<>();










}
