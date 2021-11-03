package com.mh.match.group.project.dto.response;

import com.mh.match.group.project.entity.Project;
import com.mh.match.member.dto.MemberSimpleInfoResponseDto;
import com.mh.match.group.club.dto.response.ClubInfoForSelectResponseDto;
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
public class ProjectInfoResponseDto {

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

    @ApiModelProperty(example = "[\"id\": 3, \"name\": \"박범진\", \"nickname\": \"BJP\"]")
    @ApiParam(value = "프로젝트장 정보(id, name, nickname)")
    private MemberSimpleInfoResponseDto host;

    @ApiModelProperty(example = "[{\"name\":\"python\", \"level\":\"상\", \"imgUri\":\"http://localhost:8080/api/downloadFile/97534f05-7e7f-425d-ac3e-aae8acee8a42\"}, {\"name\":\"spring\", \"level\":\"하\", \"imgUri\":\"http://localhost:8080/api/downloadFile/97534f05-7e7f-425d-ac3e-aae8acee8a42\"}]")
    @ApiParam(value = "기술 스택 리스트")
    private List<ProjectTechstackResponseDto> techstacks;

    @ApiModelProperty(example = "매주 화, 수 6시")
    @ApiParam(value = "작업 시간")
    private String schedule;

    @ApiModelProperty(example = "2020-05-22")
    @ApiParam(value = "프로젝트 마감 예정일")
    private LocalDate period;

    @ApiModelProperty(example = "3")
    @ApiParam(value = "개발자 현재 인원")
    private int developerCount;

    @ApiModelProperty(example =
        "[{\"id\": 4, \"name\": \"박범진\", \"nickname\": \"BJP\", \"email\": \"qjawls@naver.com\", \"coverPicUri\": \"http://localhost:8080/api/downloadFile/ac3e-aae8acee8a42\"},"
            + " {\"id\": 5, \"name\": \"문일민\", \"nickname\": \"IMM\", \"email\": \"IMM@naver.com\", \"coverPicUri\": \"http://localhost:8080/api/downloadFile/97534f05-7e7f\"}]")
    @ApiParam(value = "해당 프로젝트에 속해있는 개발자 정보")
    private List<MemberSimpleInfoResponseDto> developers;

    @ApiModelProperty(example = "3")
    @ApiParam(value = "개발자 모집 인원")
    private int developerMaxCount;

    @ApiModelProperty(example = "3")
    @ApiParam(value = "디자이너 현재 인원")
    private int designerCount;

    @ApiModelProperty(example =
        "[{\"id\": 4, \"name\": \"박범진\", \"nickname\": \"BJP\", \"email\": \"qjawls@naver.com\", \"coverPicUri\": \"http://localhost:8080/api/downloadFile/ac3e-aae8acee8a42\"},"
            + " {\"id\": 5, \"name\": \"문일민\", \"nickname\": \"IMM\", \"email\": \"IMM@naver.com\", \"coverPicUri\": \"http://localhost:8080/api/downloadFile/97534f05-7e7f\"}]")
    @ApiParam(value = "해당 프로젝트에 속해있는 디자이너 정보")
    private List<MemberSimpleInfoResponseDto> designers;

    @ApiModelProperty(example = "3")
    @ApiParam(value = "디자이너 모집 인원")
    private int designerMaxCount;

    @ApiModelProperty(example = "3")
    @ApiParam(value = "기획자 현재 인원")
    private int plannerCount;

    @ApiModelProperty(example =
        "[{\"id\": 4, \"name\": \"박범진\", \"nickname\": \"BJP\", \"email\": \"qjawls@naver.com\", \"coverPicUri\": \"http://localhost:8080/api/downloadFile/ac3e-aae8acee8a42\"},"
            + " {\"id\": 5, \"name\": \"문일민\", \"nickname\": \"IMM\", \"email\": \"IMM@naver.com\", \"coverPicUri\": \"http://localhost:8080/api/downloadFile/97534f05-7e7f\"}]")
    @ApiParam(value = "해당 프로젝트에 속해있는 기획자 정보")
    private List<MemberSimpleInfoResponseDto> planners;

    @ApiModelProperty(example = "3")
    @ApiParam(value = "기획자 모집 인원")
    private int plannerMaxCount;

    @ApiModelProperty(example = "경기")
    @ApiParam(value = "지역")
    private String city;

    @ApiModelProperty(example = "{\"id\": 3, \"name\": \"SSAFY\"}")
    @ApiParam(value = "소속된 클럽 정보")
    private ClubInfoForSelectResponseDto currentClub;

    @ApiModelProperty(example = "Git 매칭 프로젝트입니다.")
    @ApiParam(value = "프로젝트 소개")
    private String bio;

    @ApiModelProperty(example = "소유자")
    @ApiParam(value = "조회한 사람의 권한")
    private String authority;

    public static ProjectInfoResponseDto of(Project project,
                                            List<ProjectTechstackResponseDto> techstacks,
                                            List<MemberSimpleInfoResponseDto> developers,
                                            List<MemberSimpleInfoResponseDto> planners, List<MemberSimpleInfoResponseDto> designers,
                                            String authority) {
        return ProjectInfoResponseDto.builder()
            .id(project.getId())
            .projectProgressState(project.getProjectProgressState().getState())
            .coverPicUri(
                (project.getCoverPic() == null) ? null : project.getCoverPic().getDownload_uri())
            .name(project.getName())
            .recruitmentState(project.getRecruitmentState().getState())
            .viewCount(project.getViewCount())
            .createDate(project.getCreateDate())
            .host(MemberSimpleInfoResponseDto.from(project.getMember()))
            .techstacks(techstacks)
            .schedule(project.getSchedule())
            .period(project.getPeriod())
            .developerCount(project.getDeveloperCount())
            .developers(developers)
            .developerMaxCount(project.getDeveloperMaxCount())
            .plannerCount(project.getPlannerCount())
            .planners(planners)
            .plannerMaxCount(project.getPlannerMaxCount())
            .designerCount(project.getDesignerCount())
            .designers(designers)
            .designerMaxCount(project.getDesignerMaxCount())
            .city(project.getCity().toString())
            .currentClub((project.getClub() == null) ? null : ClubInfoForSelectResponseDto.from(
                project.getClub()))
            .bio(project.getBio())
            .authority(authority)
            .build();
    }

}
