package com.mh.match.mogakco.comment.controller;

import com.mh.match.mogakco.comment.dto.MogakcoArticleCommentRequestDto;
import com.mh.match.mogakco.comment.dto.MogakcoArticleCommentResponseDto;
import com.mh.match.mogakco.comment.service.MogakcoCommentService;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/mogakco")
public class MogakcoArticleCommentController {
    private final MogakcoCommentService mogakcoCommentService;

    @PostMapping("/{articleId}/comment/{parentId}")
    @ApiOperation(value = "댓글 작성", notes = "<strong>부모 댓글이라면 parentId는 0 입력</strong>")
    @ApiResponses({
            @ApiResponse(code = 200, message = "삭제되었습니다."),
            @ApiResponse(code = 404, message = "MEMBER_NOT_FOUND\nCOMMENT_NOT_FOUND"),
    })
    public ResponseEntity<MogakcoArticleCommentResponseDto> create(@PathVariable("articleId") Long articleId,
                                                                   @PathVariable("parentId") Long parentId, @Valid @RequestBody MogakcoArticleCommentRequestDto dto) {
        return new ResponseEntity<>(mogakcoCommentService.create(articleId, parentId, dto), HttpStatus.OK);
    }

    @GetMapping("/{articleId}/comment")
    @ApiOperation(value = "게시글 댓글 조회",
            notes = "<strong>받은 게시물 id</strong>로 게시물의 댓글 리스트를 받는다.")
    @ApiResponses({
            @ApiResponse(code = 200, message = "댓글 정보 리스트"),
            @ApiResponse(code = 404, message = "MEMBER_NOT_FOUND\nCOMMENT_NOT_FOUND"),
    })
    public ResponseEntity<List<MogakcoArticleCommentResponseDto>> getAllComment(@PathVariable("articleId") Long articleId) {
        return new ResponseEntity<>(mogakcoCommentService.allComment(articleId), HttpStatus.OK);
    }

    @PutMapping("/comment/{commentId}")
    @ApiOperation(value = "댓글 수정", notes = "<strong>받은 댓글 정보</strong>를 사용해서 댓글을 수정한다.")
    @ApiResponses({
            @ApiResponse(code = 200, message = "수정 후 정보"),
            @ApiResponse(code = 401, message = "UNAUTHORIZED_CHANGE"),
            @ApiResponse(code = 404, message = "MEMBER_NOT_FOUND\nCOMMENT_NOT_FOUND"),
    })
    public ResponseEntity<MogakcoArticleCommentResponseDto> update(@PathVariable("commentId") Long commentId,
                                                                @Valid @RequestBody MogakcoArticleCommentRequestDto dto) {
        return new ResponseEntity<>(mogakcoCommentService.update(commentId, dto), HttpStatus.OK);
    }

    @DeleteMapping("/comment/{commentId}")
    @ApiOperation(value = "댓글 삭제", notes = "<strong>받은 댓글 Id</strong>로 댓글을 삭제한다.")
    @ApiResponses({
            @ApiResponse(code = 200, message = "삭제되었습니다."),
            @ApiResponse(code = 401, message = "UNAUTHORIZED_CHANGE"),
            @ApiResponse(code = 404, message = "MEMBER_NOT_FOUND\nCOMMENT_NOT_FOUND"),
    })
    public ResponseEntity<String> delete(@PathVariable("commentId") Long commentId) {
        return new ResponseEntity<>("삭제되었습니다.", mogakcoCommentService.delete(commentId));
    }
}