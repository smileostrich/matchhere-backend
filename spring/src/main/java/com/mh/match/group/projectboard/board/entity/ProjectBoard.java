package com.mh.match.group.projectboard.board.entity;

import com.mh.match.group.projectboard.board.dto.ProjectBoardCreateRequestDto;
import com.mh.match.group.project.entity.Project;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.validation.constraints.NotBlank;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@Entity(name = "matching.project_board")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class ProjectBoard {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

//    @JsonIgnore
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "project_id")
    private Project project;

    @NotBlank
    @Column(name = "name")
    private String name;

    public void setName(String name){
        this.name = name;
    }

    public static ProjectBoard of(ProjectBoardCreateRequestDto dto, Project project){
        return ProjectBoard.builder()
            .project(project)
            .name(dto.getName())
            .build();
    }

    public ProjectBoard(String name, Project project){
        this.name = name;
        this.project = project;
    }

}
