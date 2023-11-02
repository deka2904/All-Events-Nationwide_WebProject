package com.korea.basic1.User;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserCreateForm {
    @Size(min = 3, max = 25)
    @NotEmpty(message = "사용자ID는 필수항목입니다.")
    private String userid;

    @NotEmpty(message = "비밀번호는 필수항목입니다.")
    private String password1;

    @NotEmpty(message = "비밀번호 확인은 필수항목입니다.")
    private String password2;

    @NotEmpty(message = "회원이름은 필수항목입니다.")
    private String name;

    @NotEmpty(message = "닉네임은 필수항목입니다.")
    private String nickname;

    @NotEmpty(message = "전화번호는 필수항목입니다.")
    private String phoneNumber;

    @NotEmpty(message = "주소는 필수항목입니다.")
    private String address;

    @NotEmpty(message = "이메일은 필수항목입니다.")
    @Email
    private String email;
}