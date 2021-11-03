package com.mh.match.member.dto.response;


import com.mh.match.member.entity.Certification;
import io.swagger.annotations.ApiModelProperty;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDate;

@Data
public class CertificationResponseDto {
    @ApiModelProperty(name = "id", example = "1")
    private Long id;

    @ApiModelProperty(name = "name", example = "정보처리기사")
    private String name;

    @ApiModelProperty(name = "organization", example = "한국산업인력공단")
    private String organization;

    @ApiModelProperty(name = "code", example = "D-123456")
    private String code;

    @ApiModelProperty(name = "grade", example = "level-6")
    private String grade;

    @ApiModelProperty(name = "issued_date", example = "2020-12-31")
    private LocalDate issued_date;

    @ApiModelProperty(name = "expired_date", example = "2020-12-31")
    private LocalDate expired_date;

    @ApiModelProperty(name = "is_expire", example = "true")
    private Boolean is_expire;

    public static CertificationResponseDto of(Certification certification) {
        return CertificationResponseDto.builder()
                .id(certification.getId())
                .name(certification.getName())
                .organization(certification.getOrganization())
                .code(certification.getCode())
                .grade(certification.getGrade())
                .issued_date(certification.getIssued_date())
                .expired_date(certification.getExpired_date())
                .is_expire(certification.getIs_expire())
                .build();
    }

    @Builder
    public CertificationResponseDto(Long id, String name, String organization, String code, String grade, LocalDate issued_date, LocalDate expired_date, Boolean is_expire) {
        this.id = id;
        this.name = name;
        this.organization = organization;
        this.code = code;
        this.grade = grade;
        this.issued_date = issued_date;
        this.expired_date = expired_date;
        this.is_expire = is_expire;
    }
}
