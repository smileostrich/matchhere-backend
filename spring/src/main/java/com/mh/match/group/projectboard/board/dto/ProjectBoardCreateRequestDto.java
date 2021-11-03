package com.mh.match.group.projectboard.board.dto;


import io.swagger.annotations.ApiModelProperty;
import io.swagger.annotations.ApiParam;
import javax.validation.constraints.NotEmpty;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ProjectBoardCreateRequestDto {

    @ApiModelProperty(example = "일정게시판")
    @ApiParam(value = "게시판 이름")
    @NotEmpty
    private String name;

}
