package com.alle.api.domain.member.service;

import com.alle.api.WithMockUser;
import com.alle.api.domain.member.dto.request.*;
import com.alle.api.domain.member.dto.response.FindMemberResp;
import com.alle.api.global.email.dto.request.AuthCodeVerificationRequest;
import com.alle.api.global.email.dto.request.EmailRequest;
import com.alle.api.global.email.service.EmailService;
import com.alle.api.global.security.JwtToken;
import com.alle.api.global.security.util.CookieUtils;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;


import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@AutoConfigureMockMvc
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class MemberServiceTest {

    @Autowired
    private MockMvc api;

    @Autowired
    private WebApplicationContext context;

    @MockBean
    private MemberService memberService;

    @MockBean
    private EmailService emailService;

    @MockBean
    private PasswordEncoder passwordEncoder;

    @MockBean
    private CookieUtils cookieUtils;

    @Autowired
    private ObjectMapper objectMapper;

    @BeforeEach
    void setup() {
        api = MockMvcBuilders
                .webAppContextSetup(context)
                .apply(SecurityMockMvcConfigurers.springSecurity())
                .build();
    }

    @Test
    @DisplayName("회원 가입 테스트")
    void join() throws Exception {
        // given
        SignUpReq signUpRequest = SignUpReq.builder()
                .loginId("test@test.com")
                .password("Leesunro12#")
                .passwordConfirm("Leesunro12#")
                .build();


        // when
        var resultActions = api.perform(post("/api/v1/member/join")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(signUpRequest)));

        // then
        resultActions.andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value("Sign-up successful"));
    }

    @Test
    @DisplayName("로그인 테스트")
    @WithMockUser
    void login() throws Exception {
        // given
        SignInReq loginRequest = new SignInReq("test@test.com", "Leesunro12@");
        JwtToken jwtToken = JwtToken.builder()
                .accessToken("accessToken")
                .refreshToken("refreshToken")
                .build();
        when(memberService.login(any(SignInReq.class))).thenReturn(jwtToken);

        // when
        var resultActions = api.perform(post("/api/v1/member/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(loginRequest)));

        // then
        resultActions.andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value("Login successful"));
    }

    @Test
    @DisplayName("회원 정보 조회 테스트")
    @WithMockUser
    void getMyInfo() throws Exception {
        // given
        FindMemberResp memberResp =FindMemberResp.builder()
                .id(4L)
                .email("test@test.com")
                .nickname("tester")
                .profileImg(null)
                .createdAt(null)
                .build();


        when(memberService.getMemberDetails(anyLong())).thenReturn(memberResp);

        // when
        var resultActions = api.perform(get("/api/v1/member/me")
                .contentType(MediaType.APPLICATION_JSON));

        // then
        resultActions.andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value("Member information retrieved successfully"))
                .andExpect(jsonPath("$.data.id").value(4L))
                .andExpect(jsonPath("$.data.email").value("test@test.com"))
                .andExpect(jsonPath("$.data.nickname").value("tester"));
    }

    @Test
    @DisplayName("회원 정보 수정 테스트")
    @WithMockUser
    void updateMember() throws Exception {
        // given
        UpdateReq updateReq = new UpdateReq(4L, "newNick", "newEmail@test.com");

        // when
        var resultActions = api.perform(patch("/api/v1/member/me")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(updateReq)));

        // then
        resultActions.andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value("Information updated successfully"));
    }

    @Test
    @DisplayName("로그아웃 테스트")
    @WithMockUser
    void logout() throws Exception {
        // given
        String refreshToken = "fakeRefreshToken";

        when(cookieUtils.getRefreshToken(any(HttpServletRequest.class))).thenReturn(refreshToken);
        doNothing().when(memberService).logout(anyString(), any(HttpServletResponse.class));

        // when
        var resultActions = api.perform(post("/api/v1/member/logout")
                .contentType(MediaType.APPLICATION_JSON));

        // then
        resultActions.andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value("Logout successful"));
    }

    @Test
    @DisplayName("회원 탈퇴 테스트")
    @WithMockUser
    void deleteMember() throws Exception {
        // given
        DeleteRequest deleteRequest = new DeleteRequest("Leesunro12@");

        doNothing().when(memberService).deleteMember(anyLong(), any(DeleteRequest.class));

        // when
        var resultActions = api.perform(delete("/api/v1/member/me")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(deleteRequest)));

        // then
        resultActions.andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value("Member successfully deleted"));
    }

    @Test
    @DisplayName("소셜 회원 탈퇴 테스트")
    @WithMockUser
    void deleteSocialMember() throws Exception {
        // given
        doNothing().when(memberService).deleteSocialMember(anyLong());

        // when
        var resultActions = api.perform(delete("/api/v1/member/social/me")
                .contentType(MediaType.APPLICATION_JSON));

        // then
        resultActions.andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value("Social member successfully deleted"));
    }

    @Test
    @DisplayName("이메일 인증 코드 전송 테스트")
    void sendEmailAuthCode() throws Exception {
        // given
        EmailRequest emailRequest = new EmailRequest("test@test.com");

        doNothing().when(emailService).validateMember(anyString());
        doNothing().when(emailService).sendAuthCode(anyString());

        // when
        var resultActions = api.perform(post("/api/v1/member/sendAuthCode")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(emailRequest)));

        // then
        resultActions.andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value("Verification code sent successfully"));
    }

    @Test
    @DisplayName("이메일 인증 코드 검증 테스트")
    void verifyAuthCode() throws Exception {
        // given
        AuthCodeVerificationRequest authCodeVerificationRequest = new AuthCodeVerificationRequest("test@test.com", "123456");

        doNothing().when(emailService).validateAuthCode(anyString(), anyString());

        // when
        var resultActions = api.perform(post("/api/v1/member/verifyAuthCode")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(authCodeVerificationRequest)));

        // then
        resultActions.andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value("Email verified successfully"));
    }

    @Test
    @DisplayName("임시 비밀번호 전송 테스트")
    void sendEmailTempPassword() throws Exception {
        // given
        EmailRequest emailRequest = new EmailRequest("test@test.com");

        doNothing().when(emailService).sendTemporaryPassword(anyString());

        // when
        var resultActions = api.perform(post("/api/v1/member/sendTemporaryPassword")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(emailRequest)));

        // then
        resultActions.andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value("Temporary password sent successfully"));
    }

    @Test
    @DisplayName("비밀번호 변경 실패")
    @WithMockUser
    void failUpdatingPassword() throws Exception {
        // given
        UpdatePasswordRequest passwordRequest = UpdatePasswordRequest.builder()
                .currentPassword("Leesunro12")
                .newPassword("sunro12@")
                .newPasswordConfirm("sunro12")
                .build();

        // 비밀번호가 일치하지 않는다고 설정
        when(passwordEncoder.matches(anyString(), anyString())).thenReturn(false);

        // when
        var resultActions = api.perform(patch("/api/v1/member/password")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(passwordRequest)));

        // then
        resultActions.andExpect(status().isBadRequest());
    }
}
