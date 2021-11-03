package com.mh.match.group.study.entity;

import com.mh.match.group.study.dto.request.StudyApplicationRequestDto;
import java.time.LocalDateTime;
import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Entity
@Builder
@Table(name = "matching.study_application_form")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class StudyApplicationForm {

    @EmbeddedId
    private CompositeMemberStudy compositeMemberStudy;

    @NotBlank
    @Size(min = 2, max = 30)
    private String name;

    private String bio;

    @Column(name = "create_date", nullable = false)
    private LocalDateTime createDate;

    public static StudyApplicationForm of(StudyApplicationRequestDto dto, CompositeMemberStudy cms,
        String name) {
        return StudyApplicationForm.builder()
            .compositeMemberStudy(cms)
            .name(name)
            .bio(dto.getBio())
            .createDate(LocalDateTime.now())
            .build();
    }
}