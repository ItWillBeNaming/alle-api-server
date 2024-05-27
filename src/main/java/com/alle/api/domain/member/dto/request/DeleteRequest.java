package com.alle.api.domain.member.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;

@Getter
public class DeleteRequest {

    @Schema(description = "회원 탈퇴에 필요한 비밀번호")
    private String password;
}
