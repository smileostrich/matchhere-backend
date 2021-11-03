package com.mh.match.member.dto.response;


import com.mh.match.member.entity.Career;
import io.swagger.annotations.ApiModelProperty;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDate;

@Data
public class CareerResponseDto {
    @ApiModelProperty(name = "id", example = "1")
    private Long id;

    @ApiModelProperty(name = "company", example = "Google")
    private String company;

    @ApiModelProperty(name = "role", example = "DevOps Engineer")
    private String role;

    @ApiModelProperty(name = "start_date", example = "2020-12-31")
    private LocalDate start_date;

    @ApiModelProperty(name = "end_date", example = "2020-12-31")
    private LocalDate end_date;

    @ApiModelProperty(name = "is_incumbent", example = "false")
    private Boolean is_incumbent;

    @ApiModelProperty(name = "description", example = "Google의 Lead DevOps 엔지니어로 다수의 프로젝트를 진행하였습니다.")
    private String description;

    public static CareerResponseDto of(Career career) {
        return CareerResponseDto.builder()
                .id(career.getId())
                .company(career.getCompany())
                .role(career.getRole())
                .start_date(career.getStart_date())
                .end_date(career.getEnd_date())
                .is_incumbent(career.getIs_incumbent())
                .description(career.getDescription())
                .build();
    }

    @Builder
    public CareerResponseDto(Long id, String company, String role, LocalDate start_date, LocalDate end_date, Boolean is_incumbent, String description) {
        this.id = id;
        this.company = company;
        this.role = role;
        this.start_date = start_date;
        this.end_date = end_date;
        this.is_incumbent = is_incumbent;
        this.description = description;
    }
}
