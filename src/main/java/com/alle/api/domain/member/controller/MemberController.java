package com.alle.api.domain.member.controller;

import com.alle.api.domain.member.dto.request.SignInReq;
import com.alle.api.domain.member.dto.request.SignUpReq;
import com.alle.api.domain.member.dto.response.FindMemberResp;
import com.alle.api.domain.member.service.MemberService;
import com.alle.api.global.domain.Response;
import com.alle.api.global.security.CookieUtils;
import com.alle.api.global.security.CustomUserDetail;
import com.alle.api.global.security.JwtToken;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/member")
@Slf4j
public class MemberController {

    private final MemberService memberService;

    @Operation(summary = "회원 가입", description = "일반 이메일 회원 가입입니다.",
            parameters = {
                    @Parameter(name = "email", description = "이메일", example = "test@test.com", required = true,
                            schema = @Schema(type = "string", implementation = String.class)),
                    @Parameter(name = "password", description = "비밀번호", example = "Password123!", required = true,
                            schema = @Schema(type = "string", implementation = String.class)),
                    @Parameter(name = "nickname", description = "닉네임", example = "테스트닉네임", required = true,
                            schema = @Schema(type = "string", implementation = String.class)),

            })
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "회원 가입 성공",
                    content = {@Content(schema = @Schema(implementation = Response.class))}),
            @ApiResponse(responseCode = "400", description = "회원 가입 실패 (필수 입력값을 입력하지 않은 경우, 비밀번호와 비밀번호 확인이 일치하지 않는 경우)")
    })
    @PostMapping("/join")
    public Response<Void> join(@RequestBody @Valid SignUpReq request) {
        memberService.join(request);
        return Response.success(HttpStatus.CREATED, "회원 가입 성공");
    }

    @Operation(summary = "로그인", description = "일반 로그인")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "로그인 성공",
                    content = {@Content(schema = @Schema(implementation = Response.class))}),
            @ApiResponse(responseCode = "401", description = "로그인 실패: 인증에 실패하였습니다.")
    })
    @PostMapping("/login")
    public Response<Void> login(@RequestBody SignInReq request, HttpServletResponse response) {
        JwtToken jwtToken = memberService.login(request);
        response.addHeader("Authorization", jwtToken.getAccessToken());
        CookieUtils.addCookie(response, "refreshToken", jwtToken.getRefreshToken(), 24 * 60 * 60 * 7);
        return Response.success(HttpStatus.OK, "로그인 성공");
    }

    @Operation(summary = "회원 정보 조회", description = "인증 토큰을 사용하여 회원 정보를 조회합니다.")
    @GetMapping("/me")
    public Response<FindMemberResp> findMember(@AuthenticationPrincipal CustomUserDetail user) {
        FindMemberResp member = memberService.getMemberDetails(user.getId());
        return Response.success(HttpStatus.OK, "회원 조회 성공", member);
    }


}
