package com.mh.match.group.projectboard.comment.service;

import com.mh.match.common.entity.GroupAuthority;
import com.mh.match.common.exception.CustomException;
import com.mh.match.common.exception.ErrorCode;
import com.mh.match.group.projectboard.comment.dto.ProjectArticleCommentRequestDto;
import com.mh.match.group.projectboard.comment.dto.ProjectArticleCommentResponseDto;
import com.mh.match.group.projectboard.comment.entity.ProjectArticleComment;
import com.mh.match.group.projectboard.comment.repository.ProjectArticleCommentRepository;
import com.mh.match.member.entity.Member;
import com.mh.match.member.repository.MemberRepository;
import com.mh.match.group.project.entity.CompositeMemberProject;
import com.mh.match.group.project.entity.MemberProject;
import com.mh.match.group.project.entity.Project;
import com.mh.match.group.project.repository.MemberProjectRepository;
import com.mh.match.group.projectboard.article.entity.ProjectArticle;
import com.mh.match.group.projectboard.article.repository.ProjectArticleRepository;
import com.mh.match.util.SecurityUtil;

import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true, rollbackFor = Exception.class)
@RequiredArgsConstructor
public class ProjectCommentServiceImpl implements ProjectCommentService {

    private final MemberRepository memberRepository;
    private final ProjectArticleRepository projectArticleRepository;
    private final ProjectArticleCommentRepository projectArticleCommentRepository;
    private final MemberProjectRepository memberProjectRepository;

    @Transactional
    public ProjectArticleCommentResponseDto create(Long articleId, Long parentId, ProjectArticleCommentRequestDto dto) {
        if (parentId > 0) {
            ProjectArticleComment parent = findProjectArticleComment(parentId);
            parent.addReplyCount();
        }

        ProjectArticle projectArticle = findArticle(articleId);
        // 권한 체크 (소속원인지 확인)
        Project project = projectArticle.getProjectBoard().getProject();
        Member member = findMember(SecurityUtil.getCurrentMemberId());
        checkAuthority(project, member);

        ProjectArticleComment projectArticleComment = ProjectArticleComment.of(dto, member, projectArticle);
        projectArticleComment.setDepth(parentId);
        projectArticleCommentRepository.save(projectArticleComment);

        if (parentId == 0) {
            projectArticleComment.setParentId(projectArticleComment.getId());
        }

        return ProjectArticleCommentResponseDto.from(projectArticleComment);
    }

    public List<ProjectArticleCommentResponseDto> allComment(Long articleId) {
        return projectArticleCommentRepository.findAllByProjectArticleOrderByParentIdDescCreateDateAsc(findArticle(articleId))
                .stream()
                .map(ProjectArticleCommentResponseDto::from)
                .collect(Collectors.toList());
    }

    @Transactional
    public ProjectArticleCommentResponseDto update(Long commentId, ProjectArticleCommentRequestDto dto) {
        ProjectArticleComment comment = findProjectArticleComment(commentId);

        if (!comment.getMember().getId().equals(SecurityUtil.getCurrentMemberId())) {
            throw new CustomException(ErrorCode.UNAUTHORIZED_CHANGE);
        }

        comment.setContent(dto.getContent());
        comment.setIsModified(true);

        return ProjectArticleCommentResponseDto.from(comment);
    }

    @Transactional
    public HttpStatus delete(Long commentId) {
        ProjectArticleComment comment = findProjectArticleComment(commentId);
        Member member = findMember(SecurityUtil.getCurrentMemberId());

        checkDeleteAuthority(comment.getProjectArticle().getProjectBoard().getProject(), member, comment);
        // 현재 댓글이 부모댓글이 아니라면 부모 댓글의 대댓글 수 감소
        if(comment.getParentId() != comment.getId()) {
            findProjectArticleComment(comment.getParentId()).removeReplyCount();
        }
        projectArticleCommentRepository.delete(comment);
//        comment.setIsDeleted(true);
        return HttpStatus.OK;
    }

    public Member findMember(Long memberId) {
        return memberRepository.findById(memberId)
                .orElseThrow(() -> new CustomException(ErrorCode.MEMBER_NOT_FOUND));
    }

    public ProjectArticle findArticle(Long articleId) {
        return projectArticleRepository.findById(articleId)
                .orElseThrow(() -> new CustomException(ErrorCode.ARTICLE_NOT_FOUND));
    }

    public ProjectArticleComment findProjectArticleComment(Long commentId) {
        return projectArticleCommentRepository.findById(commentId)
                .orElseThrow(() -> new CustomException(ErrorCode.COMMENT_NOT_FOUND));
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


    // 삭제 권한 (가입 여부 + 작성자 + 관리자, 소유자)
    public void checkDeleteAuthority(Project project, Member member, ProjectArticleComment comment) {
        CompositeMemberProject compositeMemberProject = new CompositeMemberProject(member, project);

        MemberProject memberProject = memberProjectRepository.findById(compositeMemberProject)
                .orElseThrow(() -> new CustomException(ErrorCode.MEMBER_CLUB_NOT_FOUND));
        if (Boolean.FALSE.equals(memberProject.getIsActive())) {
            throw new CustomException(ErrorCode.MEMBER_CLUB_NOT_FOUND);
        }

        if (!(memberProject.getAuthority().equals(GroupAuthority.소유자) ||
                memberProject.getAuthority().equals(GroupAuthority.관리자) ||
                comment.getMember().getId().equals(member.getId()))) {
            throw new CustomException(ErrorCode.UNAUTHORIZED_CHANGE);
        }

    }

}