package com.mh.match.group.project.entity;

import com.mh.match.group.project.dto.request.ProjectApplicationRequestDto;
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
@Table(name = "matching.project_application_form")
@Builder
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ProjectApplicationForm {

    @EmbeddedId
    private CompositeMemberProject compositeMemberProject;

    @NotBlank
    @Size(min = 2, max = 30)
    private String name;

    @NotBlank
    private String role;

    private String bio;

    @Column(name = "create_date", nullable = false)
    private LocalDateTime createDate;

    public static ProjectApplicationForm of(ProjectApplicationRequestDto dto, CompositeMemberProject cmp, String name){
        return ProjectApplicationForm.builder()
            .compositeMemberProject(cmp)
            .name(name)
            .role(dto.getRole())
            .bio(dto.getBio())
            .createDate(LocalDateTime.now())
            .build();
    }
}