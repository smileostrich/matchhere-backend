package com.mh.match.group.clubboard.board.dto;


import com.mh.match.group.clubboard.board.entity.ClubBoard;
import com.mh.match.group.club.entity.Club;
import io.swagger.annotations.ApiModelProperty;
import io.swagger.annotations.ApiParam;
import javax.validation.constraints.NotEmpty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class ClubBoardCreateRequestDto {


    @ApiModelProperty(name = "name", example = "일정게시판")
    @ApiParam(value = "게시판 이름", required = true)
    @NotEmpty
    private String name;

    public ClubBoard toClubBoard(Club club) {
        return ClubBoard.builder()
                .name(name)
                .club(club)
                .build();
    }
}