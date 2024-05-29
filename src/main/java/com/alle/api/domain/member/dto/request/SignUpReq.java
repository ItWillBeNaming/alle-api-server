
package com.alle.api.domain.member.dto.request;

import com.alle.api.domain.member.constant.MemberStatus;
import com.alle.api.domain.member.constant.RoleType;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.*;

import java.time.LocalDate;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SignUpReq {

    @Email(message = "이메일 형식이 올바르지 않습니다.")
    @NotBlank(message = "이메일은 필수 입력 값입니다.")
    private String loginId;

    @NotBlank(message = "비밀번호는 필수 입력 값입니다.")
    @Pattern(
            regexp = "^(?=.*[A-Za-z])(?=.*\\d)(?=.*[@$!%*#?&]).{8,20}$",
            message = "비밀번호는 8~20자의 영문 대소문자, 숫자 특수문자를 포함하여야 합니다.")
    private String password;

    private String passwordConfirm;

    @NotBlank(message = "이름은 필수 입력값입니다.")
    private String firstName;

    private String lastName;

    @NotBlank(message = "성별은 필수 입력 값입니다.")
    private String gender;

    private RoleType role;

    private MemberStatus memberStatus;

    @NotBlank(message = "닉네임은 필수 입력 값입니다.")
    @Size(max = 8, message = "닉네임은 8자 이하로 입력해야 합니다.")
    @Pattern(regexp = "^[\\p{L}0-9]+$", message = "닉네임에는 특수 문자를 사용할 수 없습니다.")
    private String nickname;


    @NotBlank(message = "생년월일은 필수 입력 값입니다.")
    private String birthDay;


    @Email(message = "이메일 형식이 올바르지 않습니다.")
    private String email;




}

