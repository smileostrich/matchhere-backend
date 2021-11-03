package com.mh.match.member.dto.response;


import com.mh.match.member.dto.inter.CareerInterface;
import com.mh.match.member.dto.inter.CertificationInterface;
import com.mh.match.member.dto.inter.EducationInterface;
import lombok.Builder;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class MemberCareerAllResponseDto {
    private List<CareerInterface> careerList = new ArrayList<>();
    private List<EducationInterface> educationList = new ArrayList<>();
    private List<CertificationInterface> certificationList = new ArrayList<>();

    public static MemberCareerAllResponseDto of(List<CareerInterface> careers, List<EducationInterface> educations, List<CertificationInterface> certifications) {
        return MemberCareerAllResponseDto.builder()
                .careerList(careers)
                .educationList(educations)
                .certificationList(certifications)
                .build();
    }

    @Builder
    public MemberCareerAllResponseDto(List<CareerInterface> careerList, List<EducationInterface> educationList, List<CertificationInterface> certificationList) {
        this.careerList = careerList;
        this.educationList = educationList;
        this.certificationList = certificationList;
    }
}
