package com.mh.match.member.controller;

import com.mh.match.member.dto.MemberResponseDto;
import com.mh.match.member.dto.request.*;
import com.mh.match.member.dto.response.LoginResponseDto;
import com.mh.match.member.service.AuthService;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {
    private final AuthService authService;

    @PostMapping("/signup")
    @ApiOperation(value = "회원가입")
    public ResponseEntity<MemberResponseDto> signup(@RequestBody @Valid SignupRequestDto signupRequestDto) throws Exception {
        return ResponseEntity.ok(authService.signup(signupRequestDto));
    }

    @PostMapping("/findpassword")
    @ApiOperation(value = "비밀번호 찾기")
    public ResponseEntity<HttpStatus> changePassword(@RequestBody @Valid ForgetChangePasswordRequestDto forgetChangePasswordRequestDto) throws Exception {
        return ResponseEntity.ok(authService.changePassword(forgetChangePasswordRequestDto));
    }

    @PostMapping("/login")
    @ApiOperation(value = "로그인")
    public ResponseEntity<LoginResponseDto> login(@RequestBody LoginRequestDto loginRequestDto) throws Exception {
        return ResponseEntity.ok(authService.login(loginRequestDto));
    }

    @PostMapping("/reissue")
    public ResponseEntity<TokenDto> reissue(@RequestBody TokenRequestDto tokenRequestDto) {
        return ResponseEntity.ok(authService.reissue(tokenRequestDto));
    }

    @GetMapping("/cert/signup/{email}")
    @ApiOperation(value = "authcode 이메일 발송")
    @ApiResponses({
            @ApiResponse(code = 200, message = "성공"),
    })
    public ResponseEntity<Long> certSignup(@PathVariable("email") String email) throws Exception {
        Long response = authService.certSignup(email);
        if (response.equals(-1L)) {
            return new ResponseEntity<>(null, HttpStatus.UNPROCESSABLE_ENTITY);
        } else {
            return new ResponseEntity<>(response, HttpStatus.OK);
        }
//        return ResponseEntity.ok(authService.certSignup(email));
    }

    @PostMapping("/cert/authcode/{id}")
    public ResponseEntity<?> emailAuthCode(@PathVariable("id") Long id, @RequestBody EmailCertRequestDto emailCertRequestDto) throws Exception {
        Long response = authService.emailAuthCode(id, emailCertRequestDto);
        if (response.equals(-1L)) {
            return new ResponseEntity<>(null, HttpStatus.UNPROCESSABLE_ENTITY);
        } else {
            return new ResponseEntity<>(response, HttpStatus.OK);
        }
    }

    @GetMapping("/cert/password/{email}")
    @ApiOperation(value = "비밀번호 찾기를 위한 authcode 이메일 발송")
    @ApiResponses({
            @ApiResponse(code = 200, message = "성공"),
    })
    public ResponseEntity<Long> passwordCertEmail(@PathVariable("email") String email) throws Exception {
        Long response = authService.certPassword(email);
        if (response.equals(-1L)) {
            return new ResponseEntity<>(null, HttpStatus.UNPROCESSABLE_ENTITY);
        } else {
            return new ResponseEntity<>(response, HttpStatus.OK);
        }
    }

    @GetMapping("/check/nickname/{nickname}")
    @ApiOperation(value = "닉네임 체크")
    @ApiResponses({
            @ApiResponse(code = 200, message = "성공"),
    })
    public ResponseEntity<Boolean> checkNickname(@PathVariable("nickname") String nickname) {
        return ResponseEntity.ok(authService.checkNickname(nickname));
    }
}