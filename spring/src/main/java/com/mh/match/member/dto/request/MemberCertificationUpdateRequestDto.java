package com.mh.match.member.dto.request;


import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.mh.match.member.entity.Certification;
import io.swagger.annotations.ApiModelProperty;
import io.swagger.annotations.ApiParam;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.time.LocalDate;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class MemberCertificationUpdateRequestDto {
    @ApiModelProperty(name = "name", example = "Certification Kubernetes Administrator")
    @ApiParam(value = "자격증명", required = true)
    @NotEmpty
    private String name;

    @ApiModelProperty(name = "organization", example = "The Linux Foundation")
    @ApiParam(value = "기관/조직", required = true)
    @NotEmpty
    private String organization;

    @ApiModelProperty(name = "code", example = "A-123456")
    @ApiParam(value = "자격증 번호", required = false)
    private String code;

    @ApiModelProperty(name = "grade", example = "Level7")
    @ApiParam(value = "등급", required = false)
    private String grade;

    @ApiModelProperty(name = "issued_date", example = "2018-05-01")
    @ApiParam(value = "취득일", required = true)
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd", timezone = "Asia/Seoul")
    @NotNull
    private LocalDate issued_date;

    @ApiModelProperty(name = "expired_date", example = "2018-05-01")
    @ApiParam(value = "만료일", required = false)
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd", timezone = "Asia/Seoul")
    private LocalDate expired_date;

    @ApiModelProperty(name = "is_expire", example = "false")
    @ApiParam(value = "만료여부", required = true)
    @NotNull
    private Boolean is_expire;

    @JsonIgnore
    public void setCertification(Certification certification) {
        certification.setName(name);
        certification.setOrganization(organization);
        certification.setCode(code);
        certification.setGrade(grade);
        certification.setIssued_date(issued_date);
        certification.setExpired_date(expired_date);
        certification.setIs_expire(is_expire);
    }
}
