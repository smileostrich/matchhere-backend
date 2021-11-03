package com.mh.match.member.dto.response;


import com.mh.match.member.entity.Member;
import lombok.Builder;
import lombok.Data;


@Data
public class MemberMeResponseDto {
    private Long id;
    private String nickname;
    private String name;
    private String email;

    public static MemberMeResponseDto of(Member member) {
        return MemberMeResponseDto.builder()
                .id(member.getId())
                .nickname(member.getNickname())
                .name(member.getName())
                .email(member.getEmail())
                .build();
    }

    @Builder
    public MemberMeResponseDto(Long id, String nickname, String name, String email) {
        this.id = id;
        this.nickname = nickname;
        this.name = name;
        this.email = email;
    }
}
