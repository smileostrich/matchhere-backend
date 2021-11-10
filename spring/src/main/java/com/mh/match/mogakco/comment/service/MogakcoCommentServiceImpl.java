package com.mh.match.mogakco.comment.service;

import com.mh.match.common.exception.CustomException;
import com.mh.match.common.exception.ErrorCode;
import com.mh.match.member.entity.Member;
import com.mh.match.member.repository.MemberRepository;
import com.mh.match.mogakco.article.entity.MogakcoArticle;
import com.mh.match.mogakco.article.repository.MogakcoArticleRepository;
import com.mh.match.mogakco.comment.dto.MogakcoArticleCommentRequestDto;
import com.mh.match.mogakco.comment.dto.MogakcoArticleCommentResponseDto;
import com.mh.match.mogakco.comment.entity.MogakcoArticleComment;
import com.mh.match.mogakco.comment.repository.MogakcoArticleCommentRepository;
import com.mh.match.util.SecurityUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional(readOnly = true, rollbackFor = Exception.class)
@RequiredArgsConstructor
public class MogakcoCommentServiceImpl implements MogakcoCommentService {
    private final MemberRepository memberRepository;
    private final MogakcoArticleRepository mogakcoArticleRepository;
    private final MogakcoArticleCommentRepository mogakcoArticleCommentRepository;

    @Transactional
    public MogakcoArticleCommentResponseDto create(Long articleId, Long parentId, MogakcoArticleCommentRequestDto dto) {
        if (parentId > 0) {
            MogakcoArticleComment parent = findMogakcoArticleComment(parentId);
            parent.addReplyCount();
        }

        MogakcoArticle mogakcoArticle = findArticle(articleId);

        Member member = findMember(SecurityUtil.getCurrentMemberId());

        MogakcoArticleComment mogakcoArticleComment = MogakcoArticleComment.of(dto, member, mogakcoArticle);
        mogakcoArticleComment.setDepth(parentId);
        mogakcoArticleCommentRepository.save(mogakcoArticleComment);

        if (parentId == 0) {
            mogakcoArticleComment.setParentId(mogakcoArticleComment.getId());
        }
        return MogakcoArticleCommentResponseDto.from(mogakcoArticleComment);
    }

    @Transactional(readOnly = true)
    public List<MogakcoArticleCommentResponseDto> allComment(Long articleId) {
        return mogakcoArticleCommentRepository.findAllByMogakcoArticleOrderByParentIdDescCreateDateAsc(findArticle(articleId))
                .stream()
                .map(MogakcoArticleCommentResponseDto::from)
                .collect(Collectors.toList());
    }

    @Transactional
    public MogakcoArticleCommentResponseDto update(Long commentId, MogakcoArticleCommentRequestDto dto) {
        MogakcoArticleComment comment = findMogakcoArticleComment(commentId);

        if (!comment.getMember().getId().equals(SecurityUtil.getCurrentMemberId())) {
            throw new CustomException(ErrorCode.UNAUTHORIZED_CHANGE);
        }
        comment.setContent(dto.getContent());
        comment.setIsModified(true);

        return MogakcoArticleCommentResponseDto.from(comment);
    }

    @Transactional
    public HttpStatus delete(Long commentId) {
        MogakcoArticleComment comment = findMogakcoArticleComment(commentId);
        if (!comment.getMember().getId().equals(SecurityUtil.getCurrentMemberId())) {
            throw new CustomException(ErrorCode.UNAUTHORIZED_CHANGE);
        }
        // 현재 댓글이 부모댓글이 아니라면 부모 댓글의 대댓글 수 감소
        if(comment.getParentId() != comment.getId()) {
            findMogakcoArticleComment(comment.getParentId()).removeReplyCount();
        }
        mogakcoArticleCommentRepository.delete(comment);
        return HttpStatus.OK;
    }

    public Member findMember(Long memberId) {
        return memberRepository.findById(memberId)
                .orElseThrow(() -> new CustomException(ErrorCode.MEMBER_NOT_FOUND));
    }

    public MogakcoArticle findArticle(Long articleId) {
        return mogakcoArticleRepository.findById(articleId).orElseThrow(() -> new CustomException(ErrorCode.ARTICLE_NOT_FOUND));
    }

    public MogakcoArticleComment findMogakcoArticleComment(Long commentId) {
        return mogakcoArticleCommentRepository.findById(commentId).orElseThrow(() -> new CustomException(ErrorCode.COMMENT_NOT_FOUND));
    }

}