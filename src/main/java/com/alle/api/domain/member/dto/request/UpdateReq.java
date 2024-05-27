package com.alle.api.domain.member.dto.request;

import lombok.Getter;

@Getter
public class UpdateReq {
    private Long id;
    private String nickname;
    private String email;

}
