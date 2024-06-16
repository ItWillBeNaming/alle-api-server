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
public class EmailRequest {


    @Email(message = "이메일 형식이 올바르지 않습니다.")
    @Schema(description = "사용자 이메일")
    private String email;
}
