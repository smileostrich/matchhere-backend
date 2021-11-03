package com.mh.match.group.project.dto.response;

import com.mh.match.group.project.entity.Project;
import com.mh.match.member.dto.MemberSimpleInfoResponseDto;
import io.swagger.annotations.ApiModelProperty;
import io.swagger.annotations.ApiParam;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@AllArgsConstructor
public class ProjectSimpleInfoResponseDto {

    @ApiModelProperty(example = "3")
    @ApiParam(value = "프로젝트 Id")
    private Long id;

    @ApiModelProperty(example = "http://localhost:8080/api/downloadFile/97534f05-7e7f-425d-ac3e-aae8acee8a42")
    @ApiParam(value = "파일 다운로드 Uri")
    private String coverPicUri;

    @ApiModelProperty(example = "알고리즘 프로젝트")
    @ApiParam(value = "프로젝트명")
    private String name;

    @ApiModelProperty(example = "프로젝트 진행 중")
    @ApiParam(value = "프로젝트 진행 상태")
    private String projectProgressState;

    @ApiModelProperty(example = "모집 중")
    @ApiParam(value = "모집 상태")
    private String recruitmentState;

    @ApiModelProperty(example = "32321")
    @ApiParam(value = "조회수")
    private int viewCount;

    @ApiModelProperty(example = "2021-09-06 06:57:37.667537")
    @ApiParam(value = "생성일자")
    private LocalDateTime createDate;

    @ApiModelProperty(example = "[\"id\": 3, \"name\": \"박범진\", \"nickname\": \"BJP\", \"email\": \"qjawls@naver.com\"]")
    @ApiParam(value = "프로젝트장 정보(id, name, nickname, email, coverPicUri)")
    private MemberSimpleInfoResponseDto host;

    @ApiModelProperty(example = "[{\"name\":\"python\", \"level\":\"상\", \"imgUri\":\"null\"}, {\"name\":\"spring\", \"level\":\"하\", \"imgUri\":\"null\"}]")
    @ApiParam(value = "기술 스택 리스트")
    private List<ProjectTechstackResponseDto> techstacks;

    @ApiModelProperty(example = "2020-05-22")
    @ApiParam(value = "프로젝트 마감 예정일")
    private LocalDate period;

    @ApiModelProperty(example = "경기")
    @ApiParam(value = "지역")
    private String city;

    @ApiModelProperty(example = "소유자")
    @ApiParam(value = "조회한 사람의 권한")
    private String authority;

    public static ProjectSimpleInfoResponseDto of(Project project,
                                                  List<ProjectTechstackResponseDto> techstacks) {
        return ProjectSimpleInfoResponseDto.builder()
            .id(project.getId())
            .name(project.getName())
            .projectProgressState(project.getProjectProgressState().getState())
            .coverPicUri(
                (project.getCoverPic() == null) ? null : project.getCoverPic().getDownload_uri())
            .recruitmentState(project.getRecruitmentState().getState())
            .viewCount(project.getViewCount())
            .createDate(project.getCreateDate())
            .host(MemberSimpleInfoResponseDto.from(project.getMember()))
            .techstacks(techstacks)
            .period(project.getPeriod())
            .city(project.getCity().toString())
            .build();
    }

}
