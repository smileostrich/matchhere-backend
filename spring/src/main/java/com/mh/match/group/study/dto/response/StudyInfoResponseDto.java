package com.mh.match.group.study.dto.response;

import com.mh.match.group.study.entity.Study;
import com.mh.match.member.dto.MemberSimpleInfoResponseDto;
import com.mh.match.s3.dto.DBFileDto;
import com.mh.match.group.club.dto.response.ClubInfoForSelectResponseDto;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import io.swagger.annotations.ApiParam;
import java.time.LocalDateTime;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@ApiModel(value = "스터디 조회 정보", description = "스터디의 상세 정보 Response Dto Class")
@Getter
@Builder
@AllArgsConstructor
public class StudyInfoResponseDto {

    @ApiModelProperty(example = "4")
    private Long id;

    @ApiModelProperty(example = "["
        + "\"id\" : 97534f05-7e7f-425d-ac3e-aae8acee8a42, "
        + "\"file_name\" : \"썸네일\", "
        + "\"file_type\" : \"img\", "
        + "\"download_uri\" : \"http://localhost:8080/api/downloadFile/97534f05-7e7f-425d-ac3e-aae8acee8a42\"]")
    @ApiParam(value = "스터디 썸네일 정보")
    private DBFileDto coverPic;

    @ApiModelProperty(example = "알고리즘 스터디")
    private String name;

    @ApiModelProperty(example = "스터디 진행 중")
    @ApiParam(value = "스터디 진행 상태")
    private String studyProgressState;

    @ApiModelProperty(example = "모집 중")
    @ApiParam(value = "모집 상태")
    private String recruitmentState;

    @ApiModelProperty(example = "32321")
    @ApiParam(value = "조회수")
    private int viewCount;

    @ApiModelProperty(example = "2021-09-06 06:57:37.667537")
    @ApiParam(value = "생성일자")
    private LocalDateTime createdDate;

    @ApiModelProperty(example = "[\"id\": 3, \"name\": \"박범진\", \"nickname\": \"BJP\"]")
    @ApiParam(value = "스터디장 정보(id, name, nickname)")
    private MemberSimpleInfoResponseDto host;

    @ApiModelProperty(example = "[{\"name\" : \"OS\"}, {\"name\" : \"DB\"}]")
    @ApiParam(value = "스터디 주제 정보")
    private List<StudyTopicResponseDto> topics;

    @ApiModelProperty(example = "매주 화, 수 6시")
    private String schedule;

    @ApiModelProperty(example = "3")
    @ApiParam(value = "스터디 현재 인원")
    private int memberCount;

    @ApiModelProperty(example = "3")
    private int maxCount;

    @ApiModelProperty(example =
        "[{\"id\": 4, \"name\": \"박범진\", \"nickname\": \"BJP\", \"email\": \"qjawls@naver.com\", \"coverPicUri\": \"http://localhost:8080/api/downloadFile/ac3e-aae8acee8a42\"},"
            + " {\"id\": 5, \"name\": \"문일민\", \"nickname\": \"IMM\", \"email\": \"IMM@naver.com\", \"coverPicUri\": \"http://localhost:8080/api/downloadFile/97534f05-7e7f\"}]")
    @ApiParam(value = "해당 스터디에 속한 인원정보")
    private List<MemberSimpleInfoResponseDto> members;

    @ApiModelProperty(example = "온라인")
    private String city;

    @ApiModelProperty(example = "{\"id\": 3, \"name\": \"SSAFY\"}")
    @ApiParam(value = "소속된 클럽 정보")
    private ClubInfoForSelectResponseDto currentClub;

    @ApiModelProperty(example = "알고리즘 스터디입니다.")
    private String bio;

    @ApiModelProperty(example = "소유자")
    @ApiParam(value = "조회한 사람의 권한")
    private String authority;

    public static StudyInfoResponseDto of(Study study, List<StudyTopicResponseDto> topics,
                                          List<MemberSimpleInfoResponseDto> members,
                                          String authority) {
        return StudyInfoResponseDto.builder()
            .id(study.getId())
            .coverPic(DBFileDto.of(study.getCoverPic()))
            .studyProgressState(study.getStudyProgressState().getState())
            .name(study.getName())
            .recruitmentState(study.getRecruitmentState().getState())
            .viewCount(study.getViewCount())
            .createdDate(study.getCreatedDate())
            .host(MemberSimpleInfoResponseDto.from(study.getMember()))
            .topics(topics)
            .schedule(study.getSchedule())
            .memberCount(study.getMemberCount())
            .maxCount(study.getMaxCount())
            .members(members)
            .city(study.getCity().value())
            .currentClub(
                (study.getClub() == null) ? null : ClubInfoForSelectResponseDto.from(study.getClub()))
            .bio(study.getBio())
            .authority(authority)
            .build();
    }

}
