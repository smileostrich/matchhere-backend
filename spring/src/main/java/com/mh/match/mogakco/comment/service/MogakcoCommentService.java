package com.mh.match.mogakco.comment.service;

import com.mh.match.mogakco.comment.dto.MogakcoArticleCommentRequestDto;
import com.mh.match.mogakco.comment.dto.MogakcoArticleCommentResponseDto;
import org.springframework.http.HttpStatus;

import java.util.List;

public interface MogakcoCommentService {

    MogakcoArticleCommentResponseDto create(Long articleId, Long parentId, MogakcoArticleCommentRequestDto dto);

    List<MogakcoArticleCommentResponseDto> allComment(Long articleId);

    MogakcoArticleCommentResponseDto update(Long commentId, MogakcoArticleCommentRequestDto dto);

    HttpStatus delete(Long commentId);
}
