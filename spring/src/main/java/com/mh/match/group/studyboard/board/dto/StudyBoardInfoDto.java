package com.mh.match.group.studyboard.board.dto;

import com.mh.match.group.studyboard.board.entity.StudyBoard;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@AllArgsConstructor
public class StudyBoardInfoDto {

    @ApiModelProperty(name="id", example = "3")
    private Integer id;

    @ApiModelProperty(name = "name", example = "일정게시판")
    private String name;

    public static StudyBoardInfoDto from(StudyBoard studyBoard){
        return StudyBoardInfoDto.builder()
            .id(studyBoard.getId())
            .name(studyBoard.getName())
            .build();
    }

}
