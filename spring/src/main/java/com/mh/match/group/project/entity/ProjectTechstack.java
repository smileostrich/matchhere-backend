package com.mh.match.group.project.entity;

import com.mh.match.common.entity.Level;

import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@Entity(name = "matching.project_techstack")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class ProjectTechstack {

    @EmbeddedId
    private CompositeProjectTechstack compositeProjectTechstack;
    @Enumerated(EnumType.STRING)
    private Level level;

    public static ProjectTechstack of(CompositeProjectTechstack compositeProjectTechstack, Level level) {
        return ProjectTechstack.builder()
            .compositeProjectTechstack(compositeProjectTechstack)
            .level(level)
            .build();
    }
}