package com.alle.api.domain.member.dto.response;


import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Data;

@Data
public class LoginResponse {

    @Schema( type = "string", example = "eyJhbGciOiJIUzUxMiJ9.eyJleHAiOjE2NTI3OTgxOTh9.6CoxHB_siOuz6PxsxHYQCgUT1_QbdyKTUwStQDutEd1-cIIARbQ0cyrnAmpIgi3IBoLRaqK7N1vXO42nYy4g5g" , description="access token 을 출력합니다.")
    private String accessToken;

    @Schema( type = "string", example ="Bearer", description="권한(Authorization) 값 해더의 명칭을 지정합니다.")
    private String tokenType = "Bearer";

    @Builder
    public LoginResponse(String accessToken) {
        this.accessToken = accessToken;
    }

}