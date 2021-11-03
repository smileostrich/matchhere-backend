package com.mh.match.group.studyboard.comment.service;

import com.mh.match.common.entity.GroupAuthority;
import com.mh.match.common.exception.CustomException;
import com.mh.match.common.exception.ErrorCode;
import com.mh.match.member.entity.Member;
import com.mh.match.member.repository.MemberRepository;
import com.mh.match.group.study.entity.CompositeMemberStudy;
import com.mh.match.group.study.entity.MemberStudy;
import com.mh.match.group.study.entity.Study;
import com.mh.match.group.study.repository.MemberStudyRepository;
import com.mh.match.group.studyboard.article.entity.StudyArticle;
import com.mh.match.group.studyboard.article.repository.StudyArticleRepository;
import com.mh.match.group.studyboard.comment.dto.StudyArticleCommentRequestDto;
import com.mh.match.group.studyboard.comment.dto.StudyArticleCommentResponseDto;
import com.mh.match.group.studyboard.comment.entity.StudyArticleComment;
import com.mh.match.group.studyboard.comment.repository.StudyArticleCommentRepository;
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
public class StudyCommentServiceImpl implements StudyCommentService{

    private final MemberRepository memberRepository;
    private final StudyArticleRepository studyArticleRepository;
    private final StudyArticleCommentRepository studyArticleCommentRepository;
    private final MemberStudyRepository memberStudyRepository;

    @Transactional
    public StudyArticleCommentResponseDto create(Long articleId, Long parentId, StudyArticleCommentRequestDto dto) {
        if (parentId > 0) {
            StudyArticleComment parent = findStudyArticleComment(parentId);
            parent.addReplyCount();
        }

        StudyArticle studyArticle = findArticle(articleId);
        // 권한 체크 (소속원인지 확인)
        Study study = studyArticle.getStudyBoard().getStudy();
        Member member = findMember(SecurityUtil.getCurrentMemberId());
        checkAuthority(study, member);

        StudyArticleComment studyArticleComment = StudyArticleComment.of(dto, member, studyArticle);
        studyArticleComment.setDepth(parentId);
        studyArticleCommentRepository.save(studyArticleComment);

        if (parentId == 0) {
            studyArticleComment.setParentId(studyArticleComment.getId());
        }

        return StudyArticleCommentResponseDto.from(studyArticleComment);
    }

    public List<StudyArticleCommentResponseDto> allComment(Long articleId) {
        return studyArticleCommentRepository.findAllByStudyArticleOrderByParentIdDescCreateDateAsc(findArticle(articleId))
                .stream()
                .map(StudyArticleCommentResponseDto::from)
                .collect(Collectors.toList());
    }

    @Transactional
    public StudyArticleCommentResponseDto update(Long commentId, StudyArticleCommentRequestDto dto) {
        StudyArticleComment comment = findStudyArticleComment(commentId);

        if (!comment.getMember().getId().equals(SecurityUtil.getCurrentMemberId())) {
            throw new CustomException(ErrorCode.UNAUTHORIZED_CHANGE);
        }

        comment.setContent(dto.getContent());
        comment.setIsModified(true);

        return StudyArticleCommentResponseDto.from(comment);
    }

    @Transactional
    public HttpStatus delete(Long commentId) {
        StudyArticleComment comment = findStudyArticleComment(commentId);
        Member member = findMember(SecurityUtil.getCurrentMemberId());

        checkDeleteAuthority(comment.getStudyArticle().getStudyBoard().getStudy(), member, comment);
        // 현재 댓글이 부모댓글이 아니라면 부모 댓글의 대댓글 수 감소
        if(comment.getParentId() != comment.getId()) {
            findStudyArticleComment(comment.getParentId()).removeReplyCount();
        }
        studyArticleCommentRepository.delete(comment);
//        comment.setIsDeleted(true);
        return HttpStatus.OK;
    }

    public Member findMember(Long memberId) {
        return memberRepository.findById(memberId)
                .orElseThrow(() -> new CustomException(ErrorCode.MEMBER_NOT_FOUND));
    }

    public StudyArticle findArticle(Long articleId) {
        return studyArticleRepository.findById(articleId)
                .orElseThrow(() -> new CustomException(ErrorCode.ARTICLE_NOT_FOUND));
    }

    public StudyArticleComment findStudyArticleComment(Long commentId) {
        return studyArticleCommentRepository.findById(commentId)
                .orElseThrow(() -> new CustomException(ErrorCode.COMMENT_NOT_FOUND));
    }

    // 가입 여부
    public void checkAuthority(Study study, Member member) {
        CompositeMemberStudy compositeMemberStudy = new CompositeMemberStudy(member, study);

        MemberStudy memberStudy = memberStudyRepository.findById(compositeMemberStudy)
                .orElseThrow(() -> new CustomException(ErrorCode.MEMBER_CLUB_NOT_FOUND));
        if (Boolean.FALSE.equals(memberStudy.getIsActive())) {
            throw new CustomException(ErrorCode.MEMBER_CLUB_NOT_FOUND);
        }

    }


    // 삭제 권한 (가입 여부 + 작성자 + 관리자, 소유자)
    public void checkDeleteAuthority(Study study, Member member, StudyArticleComment comment) {
        CompositeMemberStudy compositeMemberStudy = new CompositeMemberStudy(member, study);

        MemberStudy memberStudy = memberStudyRepository.findById(compositeMemberStudy)
                .orElseThrow(() -> new CustomException(ErrorCode.MEMBER_CLUB_NOT_FOUND));
        if (Boolean.FALSE.equals(memberStudy.getIsActive())) {
            throw new CustomException(ErrorCode.MEMBER_CLUB_NOT_FOUND);
        }

        if (!(memberStudy.getAuthority().equals(GroupAuthority.소유자) ||
                memberStudy.getAuthority().equals(GroupAuthority.관리자) ||
                comment.getMember().getId().equals(member.getId()))) {
            throw new CustomException(ErrorCode.UNAUTHORIZED_CHANGE);
        }

    }

}