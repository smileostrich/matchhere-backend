package com.mh.match.group.projectboard.board.dto;

import com.mh.match.group.projectboard.board.entity.ProjectBoard;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@AllArgsConstructor
public class ProjectBoardInfoDto {

    @ApiModelProperty(example = "3")
    private Integer id;

    @ApiModelProperty(example = "일정게시판")
    private String name;

    public static ProjectBoardInfoDto from(ProjectBoard projectBoard) {
        return ProjectBoardInfoDto.builder()
                .id(projectBoard.getId())
                .name(projectBoard.getName())
                .build();
    }

}
