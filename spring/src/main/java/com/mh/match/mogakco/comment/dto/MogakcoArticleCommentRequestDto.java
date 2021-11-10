package com.mh.match.mogakco.comment.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class MogakcoArticleCommentRequestDto {
    @ApiModelProperty(name = "content", example = "댓글 본문 내용")
    private String content;
}
