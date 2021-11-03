package com.mh.match.member.dto.request;


import com.fasterxml.jackson.annotation.JsonIgnore;
import com.mh.match.member.entity.Education;
import com.mh.match.common.annotation.Enum;
import com.mh.match.common.entity.State;
import io.swagger.annotations.ApiModelProperty;
import io.swagger.annotations.ApiParam;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class MemberEducationUpdateRequestDto {
    @ApiModelProperty(name = "institution", example = "서울대학교")
    @ApiParam(value = "학교/소속", required = true)
    @NotEmpty
    private String institution;

    @ApiModelProperty(name = "degree", example = "개발자")
    @ApiParam(value = "학위", required = true)
    @NotEmpty
    private String degree;

    @ApiModelProperty(name = "major", example = "컴퓨터과학과")
    @ApiParam(value = "전공", required = false)
    private String major;

    @ApiModelProperty(name = "start_date", example = "2018")
    @ApiParam(value = "시작일", required = true)
    @NotNull
    private Integer start_date;

    @ApiModelProperty(name = "end_date", example = "2018")
    @ApiParam(value = "종료일", required = false)
    private Integer end_date;

    @ApiModelProperty(name = "state", example = "졸업")
    @ApiParam(value = "상태", required = true)
    @Enum(enumClass = State.class, ignoreCase = false)
    @NotEmpty
    private String state;

    @ApiModelProperty(name = "description", example = "서울대학교가 배출한 최고의 인재 ooo")
    @ApiParam(value = "설명", required = false)
    private String description;

    @JsonIgnore
    public void setEducation(Education education) {
        education.setInstitution(institution);
        education.setDegree(degree);
        education.setMajor(major);
        education.setStart_date(start_date);
        education.setEnd_date(end_date);
        education.setState(state);
        education.setDescription(description);
    }
}
