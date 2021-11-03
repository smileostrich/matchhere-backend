package com.mh.match.group.study.dto.response;

import com.mh.match.group.study.entity.Study;
import com.mh.match.member.dto.MemberSimpleInfoResponseDto;
import io.swagger.annotations.ApiModelProperty;
import io.swagger.annotations.ApiParam;
import java.time.LocalDateTime;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@AllArgsConstructor
public class StudySimpleInfoResponseDto {

    @ApiModelProperty(example = "3")
    @ApiParam(value = "스터디 Id")
    private Long id;

    @ApiModelProperty(example = "http://localhost:8080/api/downloadFile/97534f05-7e7f-425d-ac3e-aae8acee8a42")
    @ApiParam(value = "파일 다운로드 Uri")
    private String coverPicUri;

    @ApiModelProperty(example = "알고리즘 스터디")
    @ApiParam(value = "스터디명")
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
    private LocalDateTime createDate;

    @ApiModelProperty(example = "[\"id\": 3, \"name\": \"박범진\", \"nickname\": \"BJP\", \"coverPicUri\": \"http://localhost:8080/api/downloadFile/97534f05-7e7f-425d-ac3e-aae8acee8a42\"]")
    @ApiParam(value = "스터디장 정보(id, name, nickname, coverPicUri)")
    private MemberSimpleInfoResponseDto host;

    @ApiModelProperty(example = "[{\"name\" : \"OS\"}, {\"name\" : \"DB\"}]")
    @ApiParam(value = "스터디 주제 정보")
    private List<StudyTopicResponseDto> topics;

    @ApiModelProperty(example = "경기")
    @ApiParam(value = "지역")
    private String city;

    public static StudySimpleInfoResponseDto of(Study study,
                                                List<StudyTopicResponseDto> topics) {
        return StudySimpleInfoResponseDto.builder()
            .id(study.getId())
            .name(study.getName())
            .studyProgressState(study.getStudyProgressState().getState())
            .coverPicUri(
                (study.getCoverPic() == null) ? null : study.getCoverPic().getDownload_uri())
            .recruitmentState(study.getRecruitmentState().getState())
            .viewCount(study.getViewCount())
            .createDate(study.getCreatedDate())
            .host(MemberSimpleInfoResponseDto.from(study.getMember()))
            .topics(topics)
            .city(study.getCity().toString())
            .build();
    }

}
