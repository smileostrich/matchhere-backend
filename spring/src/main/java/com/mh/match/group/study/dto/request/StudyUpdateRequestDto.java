package com.mh.match.group.study.dto.request;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import io.swagger.annotations.ApiParam;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@ApiModel(value = "스터디 업데이트 정보", description = "추가, 제거 기술 스택 리스트, 프로젝트명, 스케쥴, 기간, 최대인원...을 가진 Request Dto Class")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class StudyUpdateRequestDto {

//    @ApiModelProperty(example = "3fads23-fdfd13-23d2")
//    @ApiParam(value = "사진 고유 uuid")
//    private String uuid;

    @ApiModelProperty(example = "매칭 스터디")
    @ApiParam(value = "스터디명", required = true)
    @NotBlank
    @Size(min = 2, max = 100)
    private String name;

    @ApiModelProperty(example = "[\"OS\", \"DB\"]")
    @ApiParam(value = "주제")
    private List<String> topics;

    @ApiModelProperty(name = "schedule", example = "매주 화, 수 6시")
    @ApiParam(value = "스터디 작업 시간", required = true)
    private String schedule;

    @ApiModelProperty(example = "3")
    @ApiParam(value = "스터디 제한 인원", required = true)
    @NotNull
    private int maxCount;

    @ApiModelProperty(example = "스터디 진행 중")
    @ApiParam(value = "스터디 진행 상태", required = true)
    @NotBlank
    private String studyProgressState;

    @ApiModelProperty(example = "서울")
    @ApiParam(value = "지역", required = true)
    @NotBlank
    private String city;

    @ApiModelProperty(example = "3")
    @ApiParam(value = "소속된 클럽 id")
    private Long clubId;

    @ApiModelProperty(example = "알고리즘 스터디입니다.")
    @ApiParam(value = "스터디 소개", required = true)
    private String bio;

    @ApiModelProperty(example = "전체 공개")
    @ApiParam(value = "공개 범위", required = true)
    @NotBlank
    private String publicScope;

    @ApiModelProperty(example = "모집 중")
    @ApiParam(value = "모집 상태", required = true)
    @NotBlank
    private String recruitmentState;

}
