package com.example.spring_assignment.dto;

import com.example.spring_assignment.entity.UserRoleEnum;
import jakarta.validation.constraints.*;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;


@Getter
@Setter
public class SignupRequestDto {
    // 알파벳 소문자(a~z), 숫자(0~9)
    @NotBlank
    @Pattern(regexp = "^(?=.*[a-z])(?=.*\\d)[a-z\\d]{4,10}", message = "입력 양식과 맞지 않습니다.")
    private String username;

    // 알파벳 대소문자(a~z, A~Z), 숫자(0~9)
    @NotBlank
    @Pattern(regexp="^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[$@$!%*?&])[A-Za-z\\d$@$!%*?&]{8,15}", message = "입력 양식과 맞지 않습니다.")
    private String password;

    private UserRoleEnum role = UserRoleEnum.USER;
}
