package com.mh.match.portfolio.dto.request;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import io.swagger.annotations.ApiParam;

import java.time.LocalDate;
import java.util.List;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@ApiModel(value = "클럽 생성 정보", description = "클럽명, 주제, 인원, 공개 여부, 소개, 프로필 사진을 가진 Request Dto Class")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class PortfolioCreateRequestDto {

    @ApiModelProperty(example = "매칭 클럽")
    @ApiParam(value = "클럽명", required = true)
    @NotBlank
    @Size(min = 2, max = 100)
    private String name;

    @ApiModelProperty(example = "[\"플젝\", \"인맥\"]")
    @ApiParam(value = "주제")
    private List<String> tags;

    @ApiModelProperty(example = "Git 매칭 클럽입니다.")
    @ApiParam(value = "클럽 소개")
    private String bio;

    @ApiModelProperty(example = "전체 공개")
    @ApiParam(value = "공개 범위", required = true)
    @NotBlank
    private String publicScope;

    @ApiModelProperty(example = "3")
    @ApiParam(value = "모집 정원")
    @NotNull
    private int maxCount;

    @ApiModelProperty(example = "모집 중")
    @ApiParam(value = "모집 상태", required = true)
    @NotBlank
    private String recruitmentState;

    @ApiModelProperty(name = "issued_date", example = "2018-05-01")
    @ApiParam(value = "시작일", required = true)
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd", timezone = "Asia/Seoul")
    @NotNull
    private LocalDate start_date;

    @ApiModelProperty(name = "expired_date", example = "2018-05-01")
    @ApiParam(value = "종료일", required = false)
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd", timezone = "Asia/Seoul")
    private LocalDate end_date;

}
