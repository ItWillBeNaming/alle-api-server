package com.alle.api.domain.member.controller;

import com.alle.api.domain.member.domain.Member;
import com.alle.api.domain.member.dto.request.SignUpReq;
import com.alle.api.domain.member.dto.request.SocialSignUpReq;
import com.alle.api.domain.member.service.MemberService;
import com.alle.api.global.oauth.dto.GoogleOAuth2UserInfo;
import com.alle.api.global.security.service.JwtService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/member")
@Slf4j
public class MemberController {

    private final MemberService memberService;

    @PostMapping("/sign-up")
    public ResponseEntity signUp(@RequestBody SignUpReq signUpReq) {
        memberService.signUp(signUpReq);
        System.out.println("수행됨");

        return ResponseEntity.status(HttpStatus.OK).build();
    }

    @PostMapping("/social/sign-up")
    public ResponseEntity socialSignUp(@RequestBody SocialSignUpReq socialSignUpReq, HttpServletRequest request) {


        Member socialMember = memberService.signUpSocialMember(socialSignUpReq, request);


        return ResponseEntity.status(HttpStatus.OK).build();

    }

    @PatchMapping("/withdraw/{id}")
    public ResponseEntity withdraw(@PathVariable("id") long id ){

        memberService.deleteMember(id);

        return ResponseEntity.status(HttpStatus.OK).build();
    }










}
