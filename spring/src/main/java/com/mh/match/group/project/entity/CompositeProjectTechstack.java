package com.mh.match.group.project.entity;

import com.mh.match.common.entity.Techstack;

import java.io.Serializable;
import javax.persistence.Embeddable;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Embeddable
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class CompositeProjectTechstack implements Serializable {

    private static final long serialVersionUID = 6083487924395890406L;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "techstack_id")
    private Techstack techstack;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "project_id")
    private Project project;

    @Builder
    public CompositeProjectTechstack(Techstack techstack, Project project) {
        this.techstack = techstack;
        this.project = project;
    }
}