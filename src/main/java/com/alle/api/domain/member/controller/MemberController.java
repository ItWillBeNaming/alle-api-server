package com.alle.api.domain.member.controller;

import com.alle.api.domain.member.dto.request.SignUpReq;
import com.alle.api.domain.member.service.MemberService;
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

    @GetMapping("/sign-up")
    public ResponseEntity signUp() {

        return ResponseEntity.status(HttpStatus.OK).build();

    }
}
