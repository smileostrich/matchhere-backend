package com.mh.match.group.project.service;

import com.mh.match.member.entity.Member;
import com.mh.match.s3.dto.DBFileDto;
import com.mh.match.group.project.dto.response.ProjectFormSimpleInfoResponseDto;
import com.mh.match.group.project.dto.response.ProjectMemberResponseDto;
import com.mh.match.group.project.dto.response.ProjectSimpleInfoResponseDto;
import com.mh.match.group.project.dto.request.ProjectApplicationRequestDto;
import com.mh.match.group.project.dto.response.ProjectFormInfoResponseDto;
import com.mh.match.group.project.dto.request.ProjectCreateRequestDto;
import com.mh.match.group.project.dto.response.ProjectInfoForCreateResponseDto;
import com.mh.match.group.project.dto.response.ProjectInfoForUpdateResponseDto;
import com.mh.match.group.project.dto.response.ProjectInfoResponseDto;
import com.mh.match.group.project.dto.request.ProjectUpdateRequestDto;
import com.mh.match.group.project.entity.Project;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.web.multipart.MultipartFile;

public interface ProjectService {

    ProjectInfoForCreateResponseDto getInfoForCreate();

//    ProjectInfoResponseDto create(MultipartFile file, ProjectCreateRequestDto dto);
    ProjectInfoResponseDto create(MultipartFile file, ProjectCreateRequestDto dto);

    ProjectInfoResponseDto update(Long projectId, ProjectUpdateRequestDto dto);

    HttpStatus delete(Long projectId);

    Page<ProjectSimpleInfoResponseDto> getAllProject(Pageable pageable);

//    List<ProjectSimpleInfoResponseDto> getRecommendationProject(Pageable pageable);

    ProjectInfoResponseDto getOneProject(Long projectId);

    ProjectSimpleInfoResponseDto getOneSimpleProject(Long projectId);

    ProjectInfoForUpdateResponseDto getInfoForUpdateProject(Long projectId);

    List<ProjectMemberResponseDto> memberInProject(Long projectId);

    void addMember(Project project, Member member, String role);

    HttpStatus removeMe(Long projectId);

    HttpStatus removeMember(Long projectId, Long memberId);

    HttpStatus changeRole(Long projectId, Long memberId, String role);

    HttpStatus changeAuthority(Long projectId, Long memberId, String authority);

    HttpStatus checkCanApply(Long projectId);

    ProjectFormInfoResponseDto applyProject(Long projectId, ProjectApplicationRequestDto dto);

    List<ProjectFormSimpleInfoResponseDto> allProjectForm(Long projectId);

    ProjectFormInfoResponseDto oneProjectForm(Long projectId, Long memberId);

    HttpStatus approval(Long projectId, Long memberId);

    HttpStatus reject(Long projectId, Long memberId);

    HttpStatus plusViewCount(Long projectId);

    DBFileDto changeCoverPic(MultipartFile file, Long projectId);

    DBFileDto getCoverPicUri(Long projectId);

    String getMemberAuthority(Long projectId);

}
