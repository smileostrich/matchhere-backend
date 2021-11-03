package com.mh.match.group.projectboard.board.service;

import com.mh.match.common.entity.GroupAuthority;
import com.mh.match.common.exception.CustomException;
import com.mh.match.common.exception.ErrorCode;
import com.mh.match.group.projectboard.article.entity.ProjectArticle;
import com.mh.match.group.projectboard.article.entity.ProjectContent;
import com.mh.match.group.projectboard.article.repository.ProjectArticleRepository;
import com.mh.match.group.projectboard.article.repository.ProjectArticleTagRepository;
import com.mh.match.group.projectboard.article.repository.ProjectContentRepository;
import com.mh.match.group.projectboard.comment.repository.ProjectArticleCommentRepository;
import com.mh.match.member.entity.Member;
import com.mh.match.member.repository.MemberRepository;
import com.mh.match.group.project.entity.CompositeMemberProject;
import com.mh.match.group.project.entity.MemberProject;
import com.mh.match.group.project.repository.MemberProjectRepository;
import com.mh.match.group.projectboard.board.dto.ProjectBoardCreateRequestDto;
import com.mh.match.group.projectboard.board.dto.ProjectBoardInfoDto;
import com.mh.match.group.projectboard.board.dto.ProjectBoardUpdateDto;
import com.mh.match.group.projectboard.board.entity.ProjectBoard;
import com.mh.match.group.projectboard.board.repository.ProjectBoardRepository;
import com.mh.match.group.project.entity.Project;
import com.mh.match.group.project.repository.ProjectRepository;

import java.util.List;
import java.util.stream.Collectors;

import com.mh.match.util.SecurityUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class ProjectBoardService {

    private final ProjectBoardRepository projectBoardRepository;
    private final ProjectRepository projectRepository;
    private final ProjectArticleRepository projectArticleRepository;
    private final ProjectContentRepository projectContentRepository;
    private final ProjectArticleTagRepository projectArticleTagRepository;
    private final ProjectArticleCommentRepository projectArticleCommentRepository;
    private final MemberRepository memberRepository;
    private final MemberProjectRepository memberProjectRepository;

    @Transactional(readOnly = true)
    public List<ProjectBoardInfoDto> getProjectBoards(Long projectId) {
        Project project = findProject(projectId);
        Member member = findMember(SecurityUtil.getCurrentMemberId());

        // 가입 여부 확인
        checkAuthority(project, member);

        return projectBoardRepository.findAllByProject(project).stream()
                .map(ProjectBoardInfoDto::from)
                .collect(Collectors.toList());
    }

    @Transactional
    public ProjectBoardInfoDto createBoard(Long projectId, ProjectBoardCreateRequestDto dto) {
        Project project = findProject(projectId);

        // 가입 여부 + 권한(관리자 or 소유자) 확인
        checkChangeAuthority(project, findMember(SecurityUtil.getCurrentMemberId()));

        return ProjectBoardInfoDto.from(projectBoardRepository.save(ProjectBoard.of(dto, project)));
    }

    @Transactional
    public HttpStatus deleteBoard(Integer boardId) {
        ProjectBoard projectBoard = findBoard(boardId);

        // 가입 여부 + 권한(관리자 or 소유자) 확인
        checkChangeAuthority(projectBoard.getProject(), findMember(SecurityUtil.getCurrentMemberId()));

        List<ProjectArticle> projectArticles = projectArticleRepository.findAllByProjectBoard(projectBoard);

        for (ProjectArticle projectArticle: projectArticles) {
            ProjectContent projectContent = findProjectContent(projectArticle);
            projectContentRepository.delete(projectContent);
            deleteAllByProjectArticle(projectArticle);
            projectArticleTagRepository.deleteAllByProjectArticle(projectArticle);
            projectArticleRepository.delete(projectArticle);
        }

        projectBoardRepository.delete(projectBoardRepository.findById(boardId)
                .orElseThrow(() -> new CustomException(ErrorCode.BOARD_NOT_FOUND)));

        return HttpStatus.OK;
    }

    @Transactional
    public ProjectBoardInfoDto updateBoard(Integer boardId, ProjectBoardUpdateDto projectBoardUpdateDto) {
        ProjectBoard projectBoard = findBoard(boardId);

        // 가입 여부 + 권한(관리자 or 소유자) 확인
        checkChangeAuthority(projectBoard.getProject(), findMember(SecurityUtil.getCurrentMemberId()));

        projectBoard.setName(projectBoardUpdateDto.getName());
        return ProjectBoardInfoDto.from(projectBoard);
    }

    // 프로젝트 찾기
    public Project findProject(Long projectId) {
        Project project = projectRepository.findById(projectId)
                .orElseThrow(() -> new CustomException(ErrorCode.PROJECT_NOT_FOUND));

        if (Boolean.FALSE.equals(project.getIsActive())) {
            throw new CustomException(ErrorCode.DELETED_PROJECT);
        }

        return project;
    }

    // 게시판 찾기
    public ProjectBoard findBoard(int boardId) {
        return projectBoardRepository.findById(boardId)
                .orElseThrow(() -> new CustomException(ErrorCode.BOARD_NOT_FOUND));
    }

    public Member findMember(Long memberId) {
        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new CustomException(ErrorCode.MEMBER_NOT_FOUND));
        if (!member.getIs_active()) {
            throw new CustomException(ErrorCode.MEMBER_NOT_FOUND);
        }
        return member;
    }

    public ProjectArticle findProjectArticle(Long projectArticleId) {
        return projectArticleRepository.findById(projectArticleId)
                .orElseThrow(() -> new CustomException(ErrorCode.ARTICLE_NOT_FOUND));
    }

    public ProjectContent findProjectContent(ProjectArticle projectArticle) {
        return projectContentRepository.getByProjectArticle(projectArticle)
                .orElseThrow(() -> new CustomException(ErrorCode.CONTENT_NOT_FOUND));
    }

    public void deleteAllByProjectArticle(ProjectArticle projectArticle){
        projectArticleCommentRepository.deleteAllByProjectArticle(projectArticle);
    }

    // 가입 여부만 확인 (ex 조회)
    public void checkAuthority(Project project, Member member){

        CompositeMemberProject cmp = new CompositeMemberProject(member, project);
        // 가입 여부 확인
        MemberProject memberProject = memberProjectRepository.findById(cmp)
                .orElseThrow(() -> new CustomException(ErrorCode.MEMBER_PROJECT_NOT_FOUND));
        if(!memberProject.getIsActive()) throw new CustomException(ErrorCode.MEMBER_PROJECT_NOT_FOUND);

    }

    // 가입 여부 + 권한 (ex 생성 수정 삭제)
    public void checkChangeAuthority(Project project, Member member){

        CompositeMemberProject cmp = new CompositeMemberProject(member, project);
        // 가입 여부 확인
        MemberProject memberProject = memberProjectRepository.findById(cmp)
                .orElseThrow(() -> new CustomException(ErrorCode.MEMBER_PROJECT_NOT_FOUND));
        if(!memberProject.getIsActive()) throw new CustomException(ErrorCode.MEMBER_PROJECT_NOT_FOUND);

        // 팀원이면 권한 x
        if (memberProject.getAuthority().equals(GroupAuthority.팀원)) throw new CustomException(ErrorCode.UNAUTHORIZED_CHANGE);

    }
}