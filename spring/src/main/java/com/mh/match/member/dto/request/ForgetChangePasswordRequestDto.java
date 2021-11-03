package com.mh.match.member.dto.request;


import com.mh.match.member.entity.Member;
import io.swagger.annotations.ApiModelProperty;
import io.swagger.annotations.ApiParam;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class ForgetChangePasswordRequestDto {
    @ApiModelProperty(name = "id", example = "1")
    @ApiParam(value = "id", required = true)
    @NotNull
    private Long id;

    @ApiModelProperty(name = "email", example = "my_email@gmail.com")
    @ApiParam(value = "이메일", required = true)
    @Email
    @NotEmpty
    private String email;

    @ApiModelProperty(name = "password", example = "mypassword")
    @ApiParam(value = "비밀번호", required = true)
    @Pattern(regexp="(?=^.{8,255}$)(?=.*\\d)(?=.*[a-z])(?=.*[A-Z])(?=.*[!@#$%^&amp;*()_+}{&quot;:;'?/&gt;.&lt;,])(?!.*\\s).*$",
            message = "비밀번호는 영문 대,소문자와 숫자, 특수기호가 적어도 1개 이상씩 포함된 8자 ~ 255자의 비밀번호여야 합니다.")
    @NotEmpty
    private String password;

    public void setPassword(Member member, PasswordEncoder passwordEncoder) {
        member.setPassword(passwordEncoder.encode(password));
    }

}
