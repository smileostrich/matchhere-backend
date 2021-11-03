package com.mh.match.group.studyboard.comment.controller;

import com.mh.match.group.studyboard.comment.service.StudyCommentService;
import com.mh.match.group.studyboard.comment.dto.StudyArticleCommentRequestDto;
import com.mh.match.group.studyboard.comment.dto.StudyArticleCommentResponseDto;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import java.util.List;
import javax.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/studycomment")
public class StudyArticleCommentController {

    private final StudyCommentService studyCommentService;

    @PostMapping("/{articleId}/{parentId}")
    @ApiOperation(value = "댓글 작성", notes = "<strong>부모 댓글이라면 parentId는 0 입력</strong>")
    @ApiResponses({
        @ApiResponse(code = 200, message = "삭제되었습니다."),
        @ApiResponse(code = 404, message = "MEMBER_NOT_FOUND\nCOMMENT_NOT_FOUND"),
    })
    public ResponseEntity<StudyArticleCommentResponseDto> create(@PathVariable("articleId") Long articleId,
        @PathVariable("parentId") Long parentId, @Valid @RequestBody StudyArticleCommentRequestDto dto) {
        return new ResponseEntity<>(studyCommentService.create(articleId, parentId, dto), HttpStatus.OK);
    }

    @GetMapping("/{articleId}")
    @ApiOperation(value = "게시글 댓글 조회",
        notes = "<strong>받은 게시물 id</strong>로 게시물의 댓글 리스트를 받는다.")
    @ApiResponses({
        @ApiResponse(code = 200, message = "댓글 정보 리스트"),
        @ApiResponse(code = 404, message = "MEMBER_NOT_FOUND\nCOMMENT_NOT_FOUND"),
    })
    public ResponseEntity<List<StudyArticleCommentResponseDto>> getAllComment(@PathVariable("articleId") Long articleId) {
        return new ResponseEntity<>(studyCommentService.allComment(articleId), HttpStatus.OK);
    }

    @PutMapping("/{commentId}")
    @ApiOperation(value = "댓글 수정", notes = "<strong>받은 댓글 정보</strong>를 사용해서 댓글을 수정한다.")
    @ApiResponses({
        @ApiResponse(code = 200, message = "수정 후 정보"),
        @ApiResponse(code = 401, message = "UNAUTHORIZED_CHANGE"),
        @ApiResponse(code = 404, message = "MEMBER_NOT_FOUND\nCOMMENT_NOT_FOUND"),
    })
    public ResponseEntity<StudyArticleCommentResponseDto> update(@PathVariable("commentId") Long commentId,
        @Valid @RequestBody StudyArticleCommentRequestDto dto) {
        return new ResponseEntity<>(studyCommentService.update(commentId, dto), HttpStatus.OK);
    }

    @DeleteMapping("/{commentId}")
    @ApiOperation(value = "댓글 삭제", notes = "<strong>받은 댓글 Id</strong>로 댓글을 삭제한다.")
    @ApiResponses({
        @ApiResponse(code = 200, message = "삭제되었습니다."),
        @ApiResponse(code = 401, message = "UNAUTHORIZED_CHANGE"),
        @ApiResponse(code = 404, message = "MEMBER_NOT_FOUND\nCOMMENT_NOT_FOUND"),
    })
    public ResponseEntity<String> delete(@PathVariable("commentId") Long commentId) {
        return new ResponseEntity<>("삭제되었습니다.", studyCommentService.delete(commentId));
    }

}
