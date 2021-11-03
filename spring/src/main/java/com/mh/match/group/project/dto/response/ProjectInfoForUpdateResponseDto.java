package com.mh.match.group.project.dto.response;

import com.mh.match.group.project.entity.Project;
import com.mh.match.group.club.dto.response.ClubInfoForSelectResponseDto;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import io.swagger.annotations.ApiParam;
import java.time.LocalDate;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@ApiModel(value = "프로젝트 수정  정보", description = "프로젝트 수정을 위한 정보 Response Dto Class")
@Getter
@AllArgsConstructor
@Builder
public class ProjectInfoForUpdateResponseDto {

    @ApiModelProperty(example = "3")
    @ApiParam(value = "프로젝트 Id")
    private Long id;

    @ApiModelProperty(example = "알고리즘 프로젝트")
    @ApiParam(value = "프로젝트명")
    private String name;

    @ApiModelProperty(example = "프로젝트 진행 중")
    @ApiParam(value = "프로젝트 진행 상태")
    private String projectProgressState;

//    @ApiModelProperty(example = "http://localhost:8080/api/downloadFile/97534f05-7e7f-425d-ac3e-aae8acee8a42")
//    @ApiParam(value = "파일 다운로드 Uri")
//    private String coverPicUri;

    @ApiModelProperty(example = "[{\"name\":\"python\", \"level\":\"상\", \"imgUri\":\"null\"}, {\"name\":\"spring\", \"level\":\"하\", \"imgUri\":\"null\"}]")
    @ApiParam(value = "변경된 기술 스택 리스트")
    private List<ProjectTechstackResponseDto> techstacks;

    @ApiModelProperty(example = "매주 화, 수 6시")
    @ApiParam(value = "작업 시간")
    private String schedule;

    @ApiModelProperty(example = "2020-05-22")
    @ApiParam(value = "프로젝트 마감 예정일")
    private LocalDate period;

    @ApiModelProperty(example = "경기")
    @ApiParam(value = "지역")
    private String city;

    @ApiModelProperty(example = "{\"id\": 3, \"name\": \"SSAFY\"}")
    @ApiParam(value = "소속된 클럽 정보")
    private ClubInfoForSelectResponseDto currentClub;

    @ApiModelProperty(example = "[{\"clubId\": 1, \"clubName\": \"첫 클럽\"}, {\"clubId\": 2, \"clubName\": \"두번째 클럽\"}]")
    @ApiParam(value = "소유자가 속해있는 클럽 목록")
    private List<ClubInfoForSelectResponseDto> clubs;

    @ApiModelProperty(example = "Git 매칭 프로젝트입니다.")
    @ApiParam(value = "프로젝트 소개")
    private String bio;

    @ApiModelProperty(example = "클럽 멤버에게만 공개")
    @ApiParam(value = "공개 범위")
    private String publicScope;

    @ApiModelProperty(example = "모집 중")
    @ApiParam(value = "모집 상태")
    private String recruitmentState;

    @ApiModelProperty(example = "3")
    @ApiParam(value = "개발자 현재 인원")
    private int developerCount;

    @ApiModelProperty(example = "3")
    @ApiParam(value = "디자이너 현재 인원")
    private int designerCount;

    @ApiModelProperty(example = "3")
    @ApiParam(value = "기획자 현재 인원")
    private int plannerCount;

    @ApiModelProperty(example = "3")
    @ApiParam(value = "개발자 모집 인원")
    private int developerMaxCount;

    @ApiModelProperty(example = "3")
    @ApiParam(value = "디자이너 모집 인원")
    private int designerMaxCount;

    @ApiModelProperty(example = "3")
    @ApiParam(value = "기획자 모집 인원")
    private int plannerMaxCount;

    public static ProjectInfoForUpdateResponseDto of(Project project,
                                                     List<ProjectTechstackResponseDto> techstacks, List<ClubInfoForSelectResponseDto> clubs) {
        return ProjectInfoForUpdateResponseDto.builder()
            .id(project.getId())
            .name(project.getName())
            .projectProgressState(project.getProjectProgressState().getState())
            .techstacks(techstacks)
            .schedule(project.getSchedule())
            .period(project.getPeriod())
            .city(project.getCity().toString())
            .currentClub((project.getClub() == null) ? null : ClubInfoForSelectResponseDto.from(
                project.getClub()))
            .clubs(clubs)
            .bio(project.getBio())
            .publicScope(project.getPublicScope().getState())
            .recruitmentState(project.getRecruitmentState().getState())
            .developerCount(project.getDeveloperCount())
            .plannerCount(project.getPlannerCount())
            .designerCount(project.getDesignerCount())
            .developerMaxCount(project.getDeveloperMaxCount())
            .plannerMaxCount(project.getPlannerMaxCount())
            .designerMaxCount(project.getDesignerMaxCount())
            .build();
    }

}
