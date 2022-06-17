package com.example.securitybase.domain.user;

import lombok.*;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

@Getter @Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UserDto {

    @NotNull
    private String username;

//    @UniqueElements(message = "중복된 닉네임이 존재합니다 ")

    @Pattern(regexp = "^\\d{2,3}-\\d{3,4}-\\d{4}$")
    private String phone;

    @NotBlank(message = "패스워드는 필수 입력 값입니다.")
    @Pattern(regexp="(?=.*[0-9])(?=.*[a-z])(?=.*\\W)(?=\\S+$).{6,12}", message = "비밀번호는 영문자와 숫자, 특수기호가 적어도 1개 이상 포함된 6자~12자의 비밀번호여야 합니다.")
    private String password;

    @Email
    private String email;

    private String provider;

    private boolean attendState;

    public User toEntity() {
        return User.builder()
                .username(username)
                .phone(phone)
                .password(password)
                .email(email)
                .provider(provider)
                .build();
    }

}
