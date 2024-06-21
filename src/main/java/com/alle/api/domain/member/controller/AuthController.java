package com.alle.api.domain.member.controller;

import com.alle.api.domain.member.dto.request.DeleteRequest;
import com.alle.api.domain.member.dto.request.SignInReq;
import com.alle.api.domain.member.dto.request.SignUpReq;
import com.alle.api.domain.member.dto.request.UpdatePasswordRequest;
import com.alle.api.domain.member.dto.response.LoginResponse;
import com.alle.api.domain.member.service.MemberService;
import com.alle.api.global.domain.Response;
import com.alle.api.global.email.dto.request.AuthCodeVerificationRequest;
import com.alle.api.global.email.dto.request.EmailRequest;
import com.alle.api.global.email.service.EmailService;
import com.alle.api.global.security.CustomUserDetail;
import com.alle.api.global.security.JwtToken;
import com.alle.api.global.security.util.CookieUtils;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@Slf4j
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
public class AuthController {

    private final MemberService memberService;
    private final EmailService emailService;
    private final CookieUtils cookieUtils;

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
    @PostMapping("/sign-up")
    public Response<Void> signUp(@Validated @RequestBody SignUpReq request) {
        memberService.create(request);
        return Response.success(HttpStatus.CREATED);
    }

    @GetMapping("/api/users/check-email")
    public void checkEmail(
            @RequestParam @NotBlank(message = "이메일은 필수 입력 값입니다.")
            @Email(message = "이메일 형식이 올바르지 않습니다.") String email) {
        memberService.checkEmail(email);
    }

    @GetMapping("/api/users/check-nickname")
    public void checkNickname(
            @RequestParam @NotBlank(message = "닉네임은 필수 입력 값입니다.")
            @Size(max = 8, message = "닉네임은 8자 이하로 입력해야 합니다.")
            @Pattern(regexp = "^[\\p{L}0-9]+$", message = "닉네임에는 특수 문자를 사용할 수 없습니다.") String nickname) {
        memberService.checkNickname(nickname);
    }

    @Operation(summary = "로그인", description = "일반 로그인")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "로그인 성공",
                    content = {@Content(schema = @Schema(implementation = Response.class))}),
            @ApiResponse(responseCode = "401", description = "로그인 실패: 인증에 실패하였습니다.")
    })
    @PostMapping("/login")
    public Response<LoginResponse> login(@RequestBody SignInReq request, HttpServletResponse response) {
        JwtToken jwtToken = memberService.login(request);
        LoginResponse loginResponse = new LoginResponse(jwtToken.getAccessToken());
        cookieUtils.addCookie(response, "refreshToken", jwtToken.getRefreshToken(), 24 * 60 * 60 * 7);

        return Response.success(HttpStatus.OK, loginResponse);
    }


    @Operation(summary = "로그아웃", description = "쿠키에 저장된 리프레쉬 토큰을 사용하여 로그아웃")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "로그아웃 성공",
                    content = {@Content(schema = @Schema(implementation = Response.class))}),
            @ApiResponse(responseCode = "400", description = "리프레시 토큰이 쿠키에 없습니다.")
    })
    @PostMapping("/logout")
    public Response<Void> logout(HttpServletRequest request, HttpServletResponse response) {

        String refreshToken = cookieUtils.getRefreshToken(request);

        memberService.logout(refreshToken, response);

        return Response.success(HttpStatus.OK, "Logout successful");
    }

    @GetMapping("/reissue-token")
    public Response<String> reissue(HttpServletRequest request, HttpServletResponse response) {

        String refreshToken = cookieUtils.getRefreshToken(request);
        JwtToken newToken = memberService.reissueToken(refreshToken);
        cookieUtils.addCookie(response, "refreshToken", newToken.getRefreshToken(), 24 * 60 * 60 * 7);
        return Response.success(HttpStatus.OK, "Token reissued successfully", newToken.getAccessToken());
    }

    @Operation(summary = "이메일 인증 코드 전송", description = "사용자 이메일로 인증 코드를 전송합니다. 회원 존재 유무도 함께 검사합니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "이메일 인증 코드 전송 완료",
                    content = {@Content(schema = @Schema(implementation = Response.class))})
    })
    @PostMapping("/send-code")
    public Response<Void> sendEmailAuthCode(@RequestBody @Valid EmailRequest request) {
        log.info("request={}",request.getEmail());
        // 회원 인증 후 인증 코드 전송
        emailService.validateMember(request.getEmail());
        emailService.sendAuthCode(request.getEmail());
        return Response.success(HttpStatus.OK, "Verification code sent successfully");
    }

    @Operation(summary = "이메일 인증", description = "사용자 이메일로 보낸 인증 코드를 검증하여 이메일을 인증합니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "이메일 인증 성공",
                    content = {@Content(schema = @Schema(implementation = Response.class))}),
            @ApiResponse(responseCode = "400", description = "이메일 인증에 실패하였습니다.",
                    content = {@Content(schema = @Schema(implementation = Response.class))})
    })
    @PostMapping("/verify-code")
    public Response<Void> verifyAuthCode(@RequestBody @Valid AuthCodeVerificationRequest request) {
        emailService.validateAuthCode(request.getEmail(), request.getAuthCode());
        return Response.success(HttpStatus.OK, "Email verified successfully");
    }


    @Operation(summary = "임시 비밀번호 전송", description = "사용자 이메일로 임시 비밀번호를 전송합니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "임시 비밀번호 전송 완료",
                    content = {@Content(schema = @Schema(implementation = Response.class))})
    })

    @PostMapping("/temp-password")
    public Response<Void> sendEmailTempPassword(@RequestBody @Valid EmailRequest request) {
        emailService.sendTemporaryPassword(request.getEmail());
        return Response.success(HttpStatus.OK, "Temporary password sent successfully");
    }

    @Operation(summary = "일반 회원 탈퇴", description = "일반 회원은 비밀번호 확인 후 서비스 탈퇴 가능")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "탈퇴 성공",
                    content = {@Content(schema = @Schema(implementation = Response.class))})
            , @ApiResponse(responseCode = "400", description = "회원이 존재하지 않습니다.")
    })
    @DeleteMapping("/me")
    public Response<Void> deleteMember(@AuthenticationPrincipal CustomUserDetail user,
                                       @RequestBody DeleteRequest request) {
        memberService.deleteMember(user.getId(), request);
        return Response.success(HttpStatus.OK, "Member successfully deleted");
    }

    @Operation(summary = "소셜 회원 탈퇴", description = "소셜 회원은 재로그인을 통해 검증, 재발급 받은 액세스 토큰을 통해 서비스 탈퇴")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "소셜 회원 탈퇴 성공",
                    content = {@Content(schema = @Schema(implementation = Response.class))}),
            @ApiResponse(responseCode = "400", description = "해당 소셜 회원이 존재하지 않습니다.")
    })
    @DeleteMapping("/social/me")
    public Response<Void> deleteSocialMember(@AuthenticationPrincipal CustomUserDetail user) {
        memberService.deleteSocialMember(user.getId());

        return Response.success(HttpStatus.OK, "Social member successfully deleted");
    }

    @Operation(summary = "비밀번호 변경", description = "비밀번호 변경입니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "비밀번호 변경 성공",
                    content = {@Content(schema = @Schema(implementation = Response.class))}),
            @ApiResponse(responseCode = "400", description = "비밀번호 변경 실패")
    })
    @PatchMapping("/password")
    public Response<Void> updatePassword(@AuthenticationPrincipal CustomUserDetail user,
                                         @RequestBody @Valid UpdatePasswordRequest request) {
        memberService.updatePassword(user.getId(), request);
        return Response.success(HttpStatus.OK, "Password changed successfully");
    }
}
