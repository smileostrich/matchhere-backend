package com.mh.match.group.club.dto.response;


import com.mh.match.group.club.entity.MemberClub;
import com.mh.match.member.dto.MemberSimpleInfoResponseDto;
import io.swagger.annotations.ApiModelProperty;
import io.swagger.annotations.ApiParam;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class ClubMemberResponseDto {

    @ApiModelProperty(example = "[\"id\": 3, \"name\": \"박범진\", \"nickname\": \"BJP\", \"coverPicUri\":\"http://localhost:8080/api/downloadFile/97534f05-7e7f-425d-ac3e-aae8acee8a42\"]")
    @ApiParam(value = "작성자 정보(id, name, nickname, email, coverPicUri)")
    private MemberSimpleInfoResponseDto member;

    @ApiModelProperty(example = "팀원")
    @ApiParam(value = "권한")
    private String authority;

    public static ClubMemberResponseDto from(MemberClub mc) {
        return ClubMemberResponseDto.builder()
                .member(MemberSimpleInfoResponseDto.from(mc.getCompositeMemberClub().getMember()))
                .authority(mc.getAuthority().toString())
                .build();
    }
}