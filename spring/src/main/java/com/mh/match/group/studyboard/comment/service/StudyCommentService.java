package com.mh.match.group.studyboard.comment.service;

import com.mh.match.group.studyboard.comment.dto.StudyArticleCommentRequestDto;
import com.mh.match.group.studyboard.comment.dto.StudyArticleCommentResponseDto;
import java.util.List;
import org.springframework.http.HttpStatus;

public interface StudyCommentService {

    StudyArticleCommentResponseDto create(Long articleId, Long parentId, StudyArticleCommentRequestDto dto);

    List<StudyArticleCommentResponseDto> allComment(Long articleId);

    StudyArticleCommentResponseDto update(Long commentId, StudyArticleCommentRequestDto dto);

    HttpStatus delete(Long commentId);
}
