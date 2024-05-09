package com.alle.api.domain.member.controller;

import com.alle.api.domain.member.dto.request.SignInReq;
import com.alle.api.domain.member.dto.request.SignUpReq;
import com.alle.api.domain.member.service.MemberService;
import com.alle.api.global.exception.ErrorCode;
import com.alle.api.global.exception.ExceptionResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/member")
public class MemberController {

    private final MemberService memberService;

    @PostMapping("/sign-up")
    public ResponseEntity signUp(@RequestBody SignUpReq signUpReq) {
        memberService.signUp(signUpReq);
        System.out.println("수행됨");

        return ResponseEntity.status(HttpStatus.OK).build();
    }


}
