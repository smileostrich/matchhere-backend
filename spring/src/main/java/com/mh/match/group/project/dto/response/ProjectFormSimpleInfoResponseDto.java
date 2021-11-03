package com.mh.match.group.project.dto.response;

import com.mh.match.group.project.entity.ProjectApplicationForm;
import com.mh.match.member.dto.MemberSimpleInfoResponseDto;
import io.swagger.annotations.ApiModelProperty;
import io.swagger.annotations.ApiParam;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@AllArgsConstructor
public class ProjectFormSimpleInfoResponseDto {

    @ApiModelProperty(example = "4")
    @ApiParam(value = "프로젝트 id")
    private Long projectId;

    @ApiModelProperty(example = "[\"id\": 3, \"name\": \"박범진\", \"nickname\": \"BJP\", \"coverPicUri\":\"http://localhost:8080/api/downloadFile/97534f05-7e7f-425d-ac3e-aae8acee8a42\"]")
    @ApiParam(value = "작성자 정보(id, name, nickname, email, coverPicUri)")
    private MemberSimpleInfoResponseDto writer;

    @ApiModelProperty(example = "개발자")
    @ApiParam(value = "신청 역할")
    private String role;

    public static ProjectFormSimpleInfoResponseDto from(ProjectApplicationForm form){
        return ProjectFormSimpleInfoResponseDto.builder()
            .projectId(form.getCompositeMemberProject().getProject().getId())
            .writer(MemberSimpleInfoResponseDto.from(form.getCompositeMemberProject().getMember()))
            .role(form.getRole())
            .build();
    }
}
