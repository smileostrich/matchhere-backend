package com.mh.match.group.project.dto.response;

import com.mh.match.group.project.entity.MemberProject;
import com.mh.match.member.dto.MemberSimpleInfoResponseDto;
import io.swagger.annotations.ApiModelProperty;
import io.swagger.annotations.ApiParam;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class ProjectMemberResponseDto {

    @ApiModelProperty(example = "[\"id\": 3, \"name\": \"박범진\", \"nickname\": \"BJP\", \"coverPicUri\":\"http://localhost:8080/api/downloadFile/97534f05-7e7f-425d-ac3e-aae8acee8a42\"]")
    @ApiParam(value = "작성자 정보(id, name, nickname, email, coverPicUri)")
    private MemberSimpleInfoResponseDto member;

    @ApiModelProperty(example = "팀원")
    @ApiParam(value = "권한")
    private String authority;

    @ApiModelProperty(example = "개발자")
    @ApiParam(value = "역할")
    private String role;


    public static ProjectMemberResponseDto from(MemberProject mp) {
        return ProjectMemberResponseDto.builder()
            .member(MemberSimpleInfoResponseDto.from(mp.getCompositeMemberProject().getMember()))
            .authority(mp.getAuthority().toString())
            .role(mp.getRole())
            .build();
    }
}
