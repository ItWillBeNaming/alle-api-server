package com.alle.api.domain.auth.dto.request;

import com.alle.api.domain.member.constant.Gender;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

import java.time.LocalDate;

@Data
public class SignUpReq {

    @NotBlank
    private String firstName;

    private String lastName;

    @NotBlank
    @Email
    private String loginId;

    @NotBlank
    private String password;

    @Email
    private String email;

    @NotBlank
    private Gender gender;

    @NotBlank
    private LocalDate birthDate;

    @NotBlank
    private String nickName;

    private String profileImageUrl;

}
