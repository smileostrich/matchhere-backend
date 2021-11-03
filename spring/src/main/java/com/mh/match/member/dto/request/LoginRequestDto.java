package com.mh.match.member.dto.request;


import io.swagger.annotations.ApiModelProperty;
import io.swagger.annotations.ApiParam;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Pattern;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class LoginRequestDto {
    @ApiModelProperty(name = "email", example = "my_email@gmail.com")
    @ApiParam(value = "이메일", required = true)
    @NotEmpty
    private String email;

    @ApiModelProperty(name = "password", example = "mypassword")
    @ApiParam(value = "비밀번호", required = true)
    @NotEmpty
    private String password;

    public UsernamePasswordAuthenticationToken toAuthentication() {
        return new UsernamePasswordAuthenticationToken(email, password);
    }
}
