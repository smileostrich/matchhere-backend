package com.mh.match.group.project.repository;

import com.mh.match.group.project.entity.CompositeMemberProject;
import com.mh.match.group.project.entity.Project;
import com.mh.match.group.project.entity.ProjectApplicationForm;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface ProjectApplicationFormRepository extends
    JpaRepository<ProjectApplicationForm, CompositeMemberProject> {

    // 모든 신청서 날짜 내림차순 조회
//    @Query("select p from ProjectApplicationForm p where p.compositeMemberProject.project = :project order by p.createDate DESC")
//    List<ProjectApplicationForm> formByProjectId(@Param("project") Project project);

    List<ProjectApplicationForm> findAllByCompositeMemberProject_ProjectOrderByCreateDateDesc(Project project);

//    @Query("select p.compositeMemberProject.project.id as projectId, p.compositeMemberProject.member.id as memberId"
//        + ", p.name as name, p.role as role from ProjectApplicationForm p where p.compositeMemberProject.project = :project order by p.createDate DESC")
//    List<ProjectFormSimpleInfoInterfaceResponseDto> formByProjectId(@Param("project") Project project);

    // 닉네임으로 신청서 조회
//    @Query("select p from ProjectApplicationForm p "
//        + "where p.compositeMemberProject.project = :project "
//        + "and p.nickname like %:nickname% order by p.createDate desc")
//    List<ProjectApplicationForm> allFormByProjectNickname(@Param("project") Project project,
//        @Param("nickname") String nickname);

    // 신청서 조회
    @Query("select p from ProjectApplicationForm p where p.compositeMemberProject = :cmp")
    Optional<ProjectApplicationForm> oneFormById(@Param("cmp") CompositeMemberProject cmp);

}