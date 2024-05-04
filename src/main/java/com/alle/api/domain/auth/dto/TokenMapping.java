package com.alle.api.domain.auth.dto;

import lombok.Builder;
import lombok.Data;

@Data
public class TokenMapping {
    private String loginId;
    private String accessToken;
    private String refreshToken;

    public TokenMapping(){}

    @Builder
    public TokenMapping(String loginId, String accessToken, String refreshToken){
        this.loginId = loginId;
        this.accessToken = accessToken;
        this.refreshToken = refreshToken;
    }

}
