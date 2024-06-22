package com.alle.api.domain.member.controller;

import com.alle.api.domain.member.dto.request.UpdateReq;
import com.alle.api.domain.member.dto.response.FindMemberResp;
import com.alle.api.domain.member.service.MemberService;
import com.alle.api.global.domain.Response;
import com.alle.api.global.security.CustomUserDetail;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@Slf4j
@RequestMapping("/api/v1/member")
@RequiredArgsConstructor
public class MemberController {

    private final MemberService memberService;

    @Operation(summary = "회원 정보 조회", description = "인증 토큰을 사용하여 회원 정보를 조회합니다.")
    @GetMapping("/")
    public Response<FindMemberResp> findMember(@AuthenticationPrincipal CustomUserDetail user) {
        FindMemberResp member = memberService.getMemberDetails(user.getId());
        return Response.success(HttpStatus.OK, "Member information retrieved successfully", member);
    }

    //TODO:: 사진 수정도 나중에 구현하기
    @Operation(summary = "회원 정보 수정", description = "회원 정보를 수정합니다.",
            parameters = {
                    @Parameter(name = "nickname", description = "닉네임", example = "새로운 닉네임", required = true, schema = @Schema(type = "String", implementation = String.class)),
                    @Parameter(name = "email", description = "이메일", example = "새로운 이메일", schema = @Schema(type = "String", implementation = String.class))
            })
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "회원 수정 성공",
                    content = {@Content(schema = @Schema(implementation = Response.class))}),
            @ApiResponse(responseCode = "401", description = "로그인 실패: 인증에 실패하였습니다.")
    })
    @PatchMapping("/")
    public Response<Void> updateMember(@RequestBody UpdateReq updateReq) {
        memberService.updateMember(updateReq);

        return Response.success(HttpStatus.OK, "Information updated successfully");
    }
}