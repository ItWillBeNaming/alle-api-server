package com.alle.api.domain.member.dto.request;

import com.alle.api.domain.member.constant.MemberStatus;
import com.alle.api.domain.member.constant.RoleType;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.*;

import java.time.LocalDate;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SignUpReq {

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
