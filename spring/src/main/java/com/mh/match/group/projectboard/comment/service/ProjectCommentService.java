package com.mh.match.group.projectboard.comment.service;

import com.mh.match.group.projectboard.comment.dto.ProjectArticleCommentRequestDto;
import com.mh.match.group.projectboard.comment.dto.ProjectArticleCommentResponseDto;
import java.util.List;
import org.springframework.http.HttpStatus;

public interface ProjectCommentService {

    ProjectArticleCommentResponseDto create(Long articleId, Long parentId, ProjectArticleCommentRequestDto dto);

    List<ProjectArticleCommentResponseDto> allComment(Long articleId);

    ProjectArticleCommentResponseDto update(Long commentId, ProjectArticleCommentRequestDto dto);

    HttpStatus delete(Long commentId);
}
