package com.alle.api.global.email.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class AuthCodeVerificationRequest {


    @Email(message = "이메일 형식이 올바르지 않습니다.")
    @Schema(description = "인증에 사용한 이메일")
    private String email;

    @Schema(description = "이메일로 발급 받은 인증 코드")
    private String authCode;
}
