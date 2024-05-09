package com.alle.api.domain.member.dto.request;

import com.alle.api.domain.member.constant.Gender;
import com.alle.api.domain.member.constant.RoleType;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Getter
@NoArgsConstructor
public class SignUpReq {

    @Email
    private String loginId;

    private String password;


    private String firstName;

    private String lastName;

    private RoleType role;


    private String nickname;




}
