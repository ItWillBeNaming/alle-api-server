package com.alle.api.domain.member.dto.request;

import com.alle.api.domain.member.constant.MemberStatus;
import com.alle.api.domain.member.constant.RoleType;
import jakarta.validation.constraints.Email;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class SocialSignUpReq {

    @Email
    private String loginId;

    private String password;


    private String firstName;

    private String lastName;

    private RoleType role;

    private String gender;

    private MemberStatus memberStatus;

    private String nickname;

    private String birthDay;

    private String email;

}
