package com.mh.match.group.project.repository;

import com.mh.match.group.project.entity.Project;
import com.mh.match.group.project.entity.ProjectTechstack;
import com.mh.match.group.project.entity.CompositeProjectTechstack;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

public interface ProjectTechstackRepository extends JpaRepository<ProjectTechstack, CompositeProjectTechstack> {
    // 현재 프로젝트의 기술 스택 조회
    @Query("select pt from matching.project_techstack pt "
        + "where pt.compositeProjectTechstack.project = :project")
    List<ProjectTechstack> findProjectTechstackByProject(@Param("project") Project project);

    // 프로젝트의 기존 기술 스택 전체 제거
    @Transactional
    @Modifying
    @Query("delete from matching.project_techstack pt where pt.compositeProjectTechstack.project = :project")
    void deleteAllByProject(@Param("project") Project project);

}