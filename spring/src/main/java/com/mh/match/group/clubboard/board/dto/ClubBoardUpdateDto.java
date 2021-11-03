package com.mh.match.group.clubboard.board.dto;

import io.swagger.annotations.ApiModelProperty;
import io.swagger.annotations.ApiParam;
import javax.validation.constraints.NotEmpty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class ClubBoardUpdateDto {
    @ApiModelProperty(name = "name", example = "일정게시판")
    @ApiParam(value = "게시판 이름", required = true)
    @NotEmpty
    private String name;
}
