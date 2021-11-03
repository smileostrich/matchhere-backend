package com.mh.match.group.project.dto.request;

import io.swagger.annotations.ApiModelProperty;
import io.swagger.annotations.ApiParam;
import java.time.LocalDate;
import java.util.HashMap;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ProjectUpdateRequestDto {

    @ApiModelProperty(example = "매치히어")
    @ApiParam(value = "프로젝트명", required = true)
    @NotBlank
    @Size(min = 2, max = 100)
    private String name;

    @ApiModelProperty(example = "프로젝트 진행 중")
    @ApiParam(value = "프로젝트 진행 상태", required = true)
    @NotBlank
    private String projectProgressState;

//    @ApiModelProperty(example = "3fads23-fdfd13-23d2")
//    @ApiParam(value = "사진 고유 uuid")
//    private String uuid;

    @ApiModelProperty(example = "{\"python\":\"중\", \"java\":\"상\"}")
    @ApiParam(value = "변경된 기술 스택 리스트")
    private HashMap<String, String> techstacks;

    @ApiModelProperty(example = "주말 10시 ~ 18시 / 평일 논의")
    @ApiParam(value = "프로젝트 작업 시간")
    private String schedule;

    @ApiModelProperty(example = "2020-05-22")
    @ApiParam(value = "프로젝트 마감 예정일")
    private LocalDate period;

    @ApiModelProperty(example = "서울")
    @ApiParam(value = "지역", required = true)
    @NotBlank
    private String city;

    @ApiModelProperty(example = "3")
    @ApiParam(value = "소속된 클럽 id")
    private Long clubId;

    @ApiModelProperty(example = "Git 매칭 프로젝트입니다.")
    @ApiParam(value = "프로젝트 소개")
    private String bio;

    @ApiModelProperty(example = "전체 공개")
    @ApiParam(value = "공개 범위", required = true)
    @NotBlank
    private String publicScope;

    @ApiModelProperty(example = "모집 중")
    @ApiParam(value = "모집 상태", required = true)
    @NotBlank
    private String recruitmentState;

    @ApiModelProperty(example = "3")
    @ApiParam(value = "개발자 모집 인원", required = true)
    @NotNull
    private int developerMaxCount;

    @ApiModelProperty(example = "3")
    @ApiParam(value = "디자이너 모집 인원", required = true)
    @NotNull
    private int designerMaxCount;

    @ApiModelProperty(example = "3")
    @ApiParam(value = "기획자 모집 인원", required = true)
    @NotNull
    private int plannerMaxCount;

}
