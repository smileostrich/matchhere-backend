package com.mh.match.member.dto.response;

import com.mh.match.member.entity.Education;
import io.swagger.annotations.ApiModelProperty;
import lombok.Builder;
import lombok.Data;

@Data
public class EducationResponseDto {
    @ApiModelProperty(name = "id", example = "1")
    private Long id;

    @ApiModelProperty(name = "institution", example = "서울대학교")
    private String institution;

    @ApiModelProperty(name = "degree", example = "학사")
    private String degree;

    @ApiModelProperty(name = "major", example = "컴퓨터과학과")
    private String major;

    @ApiModelProperty(name = "start_date", example = "2020")
    private Integer start_date;

    @ApiModelProperty(name = "end_date", example = "2020")
    private Integer end_date;

    @ApiModelProperty(name = "state", example = "졸업")
   private String state;

    @ApiModelProperty(name = "description", example = "서울대학교가 배출한 최고의 인재")
    private String description;

    public static EducationResponseDto of(Education education) {
        return EducationResponseDto.builder()
                .id(education.getId())
                .institution(education.getInstitution())
                .degree(education.getDegree())
                .major(education.getMajor())
                .start_date(education.getStart_date())
                .end_date(education.getEnd_date())
                .state(education.getState())
                .description(education.getDescription())
                .build();
    }

    @Builder
    public EducationResponseDto(Long id, String institution, String degree, String major, Integer start_date, Integer end_date, String state, String description) {
        this.id = id;
        this.institution = institution;
        this.degree = degree;
        this.major = major;
        this.start_date = start_date;
        this.end_date = end_date;
        this.state = state;
        this.description = description;
    }
}
