package com.mh.match.group.clubboard.board.dto;

import com.mh.match.group.clubboard.board.entity.ClubBoard;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@AllArgsConstructor
public class ClubBoardInfoDto {

    @ApiModelProperty(name="id", example = "3")
    private Integer id;

    @ApiModelProperty(name = "name", example = "일정게시판")
    private String name;

    public static ClubBoardInfoDto from(ClubBoard clubBoard) {
        return ClubBoardInfoDto.builder()
                .id(clubBoard.getId())
                .name(clubBoard.getName())
                .build();
    }

}
