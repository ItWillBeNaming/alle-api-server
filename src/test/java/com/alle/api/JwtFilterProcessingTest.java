package com.alle.api;


import com.alle.api.domain.member.constant.Gender;
import com.alle.api.domain.member.constant.RoleType;
import com.alle.api.domain.member.domain.Member;
import com.alle.api.domain.member.repository.MemberRepository;
import com.alle.api.global.security.service.JwtService;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.persistence.EntityManager;
import jakarta.transaction.Transactional;
import jdk.jfr.Description;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.internal.invocation.MockitoMethod;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@Transactional
public class JwtFilterProcessingTest {

    @Autowired
    MockMvc mockMvc;

    @Autowired
    MemberRepository memberRepository;

    @Autowired
    JwtService jwtService;

    @Autowired
    EntityManager em;

    private ObjectMapper objectMapper = new ObjectMapper();

    PasswordEncoder BcryptPasswordEncoder = new BCryptPasswordEncoder();

    @Value("${jwt.secretKey}")
    private String secretKey;

    @Value("${jwt.access.header}")
    private String accessHeader;

    @Value("${jwt.refresh.header")
    private String refreshHeader;

    private static final String KEY_EMAIL = "loginId";
    private static final String KEY_PASSWORD = "password";
    private static final String LOGIN_ID = "test@naver.com";
    private static final String PASSWORD = "1234";
    private static final String LOGIN_URL = "/login";
    private static final String ACCESS_TOKEN_SUBJECT = "AccessToken";
    private static final String REFRESH_TOKEN_SUBJECT = "RefreshToken";
    private static final String BEARER = "Bearer ";

    /**
     * 매 테스트 시작 전에 유저 데이터 생성
     */

    @BeforeEach
    public void init() {
        Member member = Member.builder()
                .loginId(LOGIN_ID).password(BcryptPasswordEncoder.encode(PASSWORD))
                .birthDay(LocalDate.parse("1994-11-29", DateTimeFormatter.ofPattern("yyyy-MM-dd"))).email("test@test.com").createdDate(LocalDateTime.now()).firstName("Lee").lastName("Sunro").gender(Gender.MALE)
                .nickname("tt").role(RoleType.USER).build();

        memberRepository.save(member);

    }

    private void clear() {
        em.flush();
        em.clear();
    }

    private Map<String, String> getUsernameAndPaswwordMap(String loginId, String password) {
        Map<String, String> usernamePasswordMap = new LinkedHashMap<>();
        usernamePasswordMap.put(KEY_EMAIL, loginId);
        usernamePasswordMap.put(KEY_PASSWORD, password);

        return usernamePasswordMap;
    }

    private Map<String, String> getTokenMap() throws Exception {
        Map<String, String> usernamePasswordMap = getUsernameAndPaswwordMap(LOGIN_ID, PASSWORD);

        MvcResult result = mockMvc.perform(post(LOGIN_URL).contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(usernamePasswordMap))).andReturn();


        String accesstoken = result.getResponse().getHeader(accessHeader);
        String refreshToken = result.getResponse().getHeader(refreshHeader);

        if (accesstoken == null || refreshToken == null) {
            throw new RuntimeException("Failed to retrives tokens");
        }

        Map<String, String> tokenMap = new HashMap<>();
        tokenMap.put(accessHeader, accesstoken);
        tokenMap.put(refreshHeader, refreshToken);

        return tokenMap;
    }

    @Test
    @DisplayName("AccessToken, RefeshToken 모두 존재하지 않는 경우")
    void Access_Refresh_not_exist() throws Exception {
        mockMvc.perform(get("/jwt-test"))
                .andExpect(status().isFound());
    }

    @Test
    @DisplayName("유효한 AccessToken만 요청 - 200 인증성공")
    void Access_valid_request() throws Exception {
        //given
        Map<String, String> tokenMap = new HashMap<>();
        String accessToken = tokenMap.get(accessHeader);

        //when,then
        mockMvc.perform(get("/jwt-test").header(accessHeader, BEARER + accessToken)).andExpect(status().isOk());



    }
}
