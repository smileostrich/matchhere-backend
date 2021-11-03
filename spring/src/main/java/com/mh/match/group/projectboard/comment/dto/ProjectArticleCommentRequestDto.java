package com.mh.match.group.projectboard.comment.dto;

import io.swagger.annotations.ApiModelProperty;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ProjectArticleCommentRequestDto {

    @ApiModelProperty(name = "content", example = "댓글 본문 내용")
    @NotBlank
    @Size(max = 500)
    private String content;
}
