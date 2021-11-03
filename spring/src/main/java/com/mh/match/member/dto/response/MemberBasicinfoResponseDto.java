package com.mh.match.member.dto.response;


import com.mh.match.member.entity.Member;
import com.mh.match.s3.entity.DBFile;
import lombok.*;

@Data
public class MemberBasicinfoResponseDto {
//    private DBFile cover_pic;
    private String nickname;
    private String name;
    private String city;
    private String bio;
    private String email;

    public static MemberBasicinfoResponseDto of(Member member) {
        return MemberBasicinfoResponseDto.builder()
//                .cover_pic(member.getCover_pic())
                .nickname(member.getNickname())
                .name(member.getName())
                .city(member.getCity())
                .bio(member.getBio())
                .email(member.getEmail())
                .build();
    }

    @Builder
    public MemberBasicinfoResponseDto(String nickname, String name, String city, String bio, String email) {
//        this.cover_pic = cover_pic;
        this.nickname = nickname;
        this.name = name;
        this.city = city;
        this.bio = bio;
        this.email = email;
    }
}