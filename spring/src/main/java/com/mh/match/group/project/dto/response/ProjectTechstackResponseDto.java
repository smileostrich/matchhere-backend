package com.mh.match.group.project.dto.response;

import com.mh.match.group.project.entity.ProjectTechstack;
import io.swagger.annotations.ApiModelProperty;
import io.swagger.annotations.ApiParam;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@AllArgsConstructor
public class ProjectTechstackResponseDto {

    @ApiModelProperty(example = "spring")
    @ApiParam(value = "기술 스택명")
    private String name;

    @ApiModelProperty(example = "상")
    @ApiParam(value = "수준")
    private String level;

    @ApiModelProperty(example = "http://localhost:8080/api/downloadFile/97534f05-7e7f-425d-ac3e-aae8acee8a42")
    @ApiParam(value = "기술 스택 이미지 URI")
    private String imgUri;

    public static ProjectTechstackResponseDto simple(ProjectTechstack pt){
        return ProjectTechstackResponseDto.builder()
            .name(pt.getCompositeProjectTechstack().getTechstack().getName())
            .level(pt.getLevel().toString())
            .build();
    }

    public static ProjectTechstackResponseDto full(ProjectTechstack pt){
        return ProjectTechstackResponseDto.builder()
            .name(pt.getCompositeProjectTechstack().getTechstack().getName())
            .level(pt.getLevel().toString())
            .imgUri(pt.getCompositeProjectTechstack().getTechstack().getImg_uri())
            .build();
    }

}
