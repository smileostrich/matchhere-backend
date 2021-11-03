package com.mh.match.group.projectboard.article.service;


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
import com.mh.match.group.project.entity.Project;
import com.mh.match.group.project.repository.MemberProjectRepository;
import com.mh.match.group.projectboard.article.dto.ProjectArticleInfoResponseDto;
import com.mh.match.group.projectboard.article.dto.ProjectArticleSimpleInfoResponseDto;
import com.mh.match.group.projectboard.article.dto.ProjectArticleRequestDto;
import com.mh.match.group.projectboard.article.entity.ProjectArticleTag;
import com.mh.match.group.projectboard.board.entity.ProjectBoard;
import com.mh.match.group.projectboard.board.repository.ProjectBoardRepository;
import com.mh.match.util.SecurityUtil;
import java.util.ArrayList;
import java.util.List;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class ProjectArticleService {

    private final ProjectBoardRepository projectBoardRepository;
    private final ProjectArticleRepository projectArticleRepository;
    private final ProjectContentRepository projectContentRepository;
    private final MemberRepository memberRepository;
    private final ProjectArticleTagRepository projectArticleTagRepository;
    private final ProjectArticleCommentRepository projectArticleCommentRepository;
    private final MemberProjectRepository memberProjectRepository;

    @Transactional(readOnly = true)
    public ProjectArticleInfoResponseDto getProjectArticleDetail(Long articleId) {
        ProjectArticle projectArticle = findProjectArticle(articleId);

        // 권한 체크 (소속원인지 확인)
        checkAuthority(projectArticle.getProjectBoard().getProject(), findMember(SecurityUtil.getCurrentMemberId()));

        ProjectArticleInfoResponseDto projectArticleInfoResponseDto = ProjectArticleInfoResponseDto.of(
                projectArticle, getProjectArticleTagList(projectArticle));
        ProjectContent projectContent = findProjectContent(projectArticle);
        projectArticleInfoResponseDto.setContent(projectContent.getContent());
        return projectArticleInfoResponseDto;
    }

    public List<String> getProjectArticleTagList(ProjectArticle projectArticle) {
        List<ProjectArticleTag> pats = projectArticleTagRepository.findAllByProjectArticle(
                projectArticle);
        List<String> tags = new ArrayList<>();
        for (ProjectArticleTag pat : pats) {
            tags.add(pat.getName());
        }
        return tags;
    }

    @Transactional(readOnly = true)
    public Page<ProjectArticleSimpleInfoResponseDto> getProjectArticles(Integer boardId, Pageable pageable) {
        ProjectBoard projectBoard = findProjectBoard(boardId);
        // 권한 체크 (소속원인지 확인)
        checkAuthority(projectBoard.getProject(), findMember(SecurityUtil.getCurrentMemberId()));

        Page<ProjectArticle> projectArticles = projectArticleRepository.findAllByProjectBoard(projectBoard,
                pageable);
        return projectArticles.map(
                m -> ProjectArticleSimpleInfoResponseDto.of(m, getProjectArticleTagList(m)));
    }

    @Transactional
    public ProjectArticleInfoResponseDto createArticle(ProjectArticleRequestDto dto) {
        ProjectBoard projectBoard = findProjectBoard(dto.getProjectBoardId());
        // 권한 체크 (소속원인지 확인)
        Project project = projectBoard.getProject();
        Member member = findMember(SecurityUtil.getCurrentMemberId());
        checkAuthority(project, member);

        if (dto.getContent() == null) {
            throw new CustomException(ErrorCode.CONTENT_NOT_FOUND);
        }
        ProjectArticle projectArticle = projectArticleRepository.save(
                ProjectArticle.of(dto, projectBoard, member));
        addContent(projectArticle, dto.getContent());
        addTags(projectArticle, dto.getTags());

        ProjectArticleInfoResponseDto projectArticleInfoResponseDto = ProjectArticleInfoResponseDto.of(
                projectArticle, dto.getTags());
        projectArticleInfoResponseDto.setContent(dto.getContent());
        return projectArticleInfoResponseDto;
    }

    @Transactional
    public ProjectArticleInfoResponseDto updateArticle(Long articleId, ProjectArticleRequestDto dto) {
        ProjectArticle projectArticle = findProjectArticle(articleId);

        // 권한 체크 (소속원인지 확인)
        checkUpdateAuthority(projectArticle.getProjectBoard().getProject(),
                findMember(SecurityUtil.getCurrentMemberId()), projectArticle);

        if (dto.getContent() == null) {
            throw new CustomException(ErrorCode.CONTENT_NOT_FOUND);
        }

        // 게시글 내용
        ProjectContent projectContent = findProjectContent(projectArticle);
        projectContent.setContent(dto.getContent());

        projectArticle.update(dto, findProjectBoard(dto.getProjectBoardId()));
        addTags(projectArticle, dto.getTags());

        ProjectArticleInfoResponseDto projectArticleInfoResponseDto = ProjectArticleInfoResponseDto.of(
                projectArticle, dto.getTags());
        projectArticleInfoResponseDto.setContent(projectContent.getContent());
        return projectArticleInfoResponseDto;
    }

    @Transactional
    public HttpStatus deleteArticle(Long articleId) {
        ProjectArticle projectArticle = findProjectArticle(articleId);

        // 소속원이고 작성자이거나 소유자, 관리자인지
        checkDeleteAuthority(projectArticle.getProjectBoard().getProject(),
                findMember(SecurityUtil.getCurrentMemberId()), projectArticle);

        ProjectContent projectContent = findProjectContent(projectArticle);
        deleteAllCommentByProjectArticle(projectArticle);
        projectArticleTagRepository.deleteAllByProjectArticle(projectArticle);
        projectContentRepository.delete(projectContent);
        projectArticleRepository.delete(projectArticle);

        return HttpStatus.OK;
    }

    @Transactional
    public void addContent(ProjectArticle projectArticle, String content) {
        projectContentRepository.save(ProjectContent.of(projectArticle, content));
    }

    // 클럽 게시글 조회수 증가
    @Transactional
    public HttpStatus plusViewCount(Long projectArticleId) {
        findProjectArticle(projectArticleId).plusViewCount();
        return HttpStatus.OK;
    }

    public ProjectBoard findProjectBoard(int boardId) {
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

    public void deleteAllCommentByProjectArticle(ProjectArticle projectArticle) {
        projectArticleCommentRepository.deleteAllByProjectArticle(projectArticle);
    }

    // 게시글 태그 추가
    @Transactional
    public void addTags(ProjectArticle projectArticle, List<String> tags) {
        projectArticleTagRepository.deleteAllByProjectArticle(projectArticle);
        if (tags == null) {
            return;
        }

        for (String tag : tags) {
            projectArticleTagRepository.save(ProjectArticleTag.of(projectArticle, tag));
        }

    }

    // 가입 여부
    public void checkAuthority(Project project, Member member) {
        CompositeMemberProject compositeMemberProject = new CompositeMemberProject(member, project);

        MemberProject memberProject = memberProjectRepository.findById(compositeMemberProject)
                .orElseThrow(() -> new CustomException(ErrorCode.MEMBER_CLUB_NOT_FOUND));
        if (Boolean.FALSE.equals(memberProject.getIsActive())) {
            throw new CustomException(ErrorCode.MEMBER_CLUB_NOT_FOUND);
        }

    }

    // 수정 권한 (가입 여부 + 작성자 확인)
    public void checkUpdateAuthority(Project project, Member member, ProjectArticle projectArticle) {
        CompositeMemberProject compositeMemberProject = new CompositeMemberProject(member, project);

        MemberProject memberProject = memberProjectRepository.findById(compositeMemberProject)
                .orElseThrow(() -> new CustomException(ErrorCode.MEMBER_CLUB_NOT_FOUND));
        if (Boolean.FALSE.equals(memberProject.getIsActive())) {
            throw new CustomException(ErrorCode.MEMBER_CLUB_NOT_FOUND);
        }

        if (!projectArticle.getMember().getId().equals(member.getId())) {
            throw new CustomException(ErrorCode.UNAUTHORIZED_CHANGE);
        }

    }

    // 삭제 권한 (가입 여부 + 작성자 + 관리자, 소유자)
    public void checkDeleteAuthority(Project project, Member member, ProjectArticle projectArticle) {
        CompositeMemberProject compositeMemberProject = new CompositeMemberProject(member, project);

        MemberProject memberProject = memberProjectRepository.findById(compositeMemberProject)
                .orElseThrow(() -> new CustomException(ErrorCode.MEMBER_CLUB_NOT_FOUND));
        if (Boolean.FALSE.equals(memberProject.getIsActive())) {
            throw new CustomException(ErrorCode.MEMBER_CLUB_NOT_FOUND);
        }

        if (!(memberProject.getAuthority().equals(GroupAuthority.소유자) ||
                memberProject.getAuthority().equals(GroupAuthority.관리자) ||
                projectArticle.getMember().getId().equals(member.getId()))) {
            throw new CustomException(ErrorCode.UNAUTHORIZED_CHANGE);
        }

    }

}