package com.alle.api.domain.member.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class SignInReq {

    @Schema( type = "string", example = "string@aa.bb", description="계정 이메일 입니다.")
    @NotBlank
    @NotNull
    @Email
    private String loginId;

    @Schema( type = "string", example = "string", description="계정 비밀번호 입니다.")
    @NotBlank
    @NotNull
    private String password;


}
