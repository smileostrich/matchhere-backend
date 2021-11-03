package com.mh.match.group.clubboard.comment.service;

import com.mh.match.group.clubboard.comment.dto.ClubArticleCommentRequestDto;
import com.mh.match.group.clubboard.comment.dto.ClubArticleCommentResponseDto;
import java.util.List;
import org.springframework.http.HttpStatus;

public interface ClubCommentService {

    ClubArticleCommentResponseDto create(Long articleId, Long parentId, ClubArticleCommentRequestDto dto);

    List<ClubArticleCommentResponseDto> allComment(Long articleId);

    ClubArticleCommentResponseDto update(Long commentId, ClubArticleCommentRequestDto dto);

    HttpStatus delete(Long commentId);
}
