package com.mh.match.mogakco.dto;

import io.swagger.annotations.ApiModelProperty;
import io.swagger.annotations.ApiParam;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.List;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class MogakcoArticleRequestDto {

    @ApiModelProperty(example = "게시글 제목")
    @ApiParam(value = "게시글 제목")
    @NotBlank
    @Size(min = 2, max = 45)
    private String title;

    @ApiModelProperty(example = "내용~~~~")
    @ApiParam(value = "게시글 내용")
    @NotBlank
    private String content;

    @ApiModelProperty(example = "[\"태그1\", \"태그2\"]")
    @ApiParam(value = "게시글 태그")
    private List<String> tags;

}
