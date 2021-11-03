package com.mh.match.member.dto.response;

import com.mh.match.member.entity.Member;
import com.mh.match.member.dto.request.TokenDto;
import lombok.Builder;
import lombok.Data;

@Data
public class LoginResponseDto {
    private TokenDto tokenInfo;
    private Long id;
    private String nickname;
    private String name;
    private String email;

    public static LoginResponseDto of(Member member, TokenDto tokenDto) {
        return LoginResponseDto.builder()
                .tokenInfo(tokenDto)
                .id(member.getId())
                .nickname(member.getNickname())
                .name(member.getName())
                .email(member.getEmail())
                .build();
    }

    @Builder
    public LoginResponseDto(TokenDto tokenInfo, Long id, String nickname, String name, String email) {
        this.tokenInfo = tokenInfo;
        this.id = id;
        this.nickname = nickname;
        this.name = name;
        this.email = email;
    }
}
