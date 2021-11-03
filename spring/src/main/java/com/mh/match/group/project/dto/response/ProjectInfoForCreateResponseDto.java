package com.mh.match.group.project.dto.response;

import com.mh.match.group.club.dto.response.ClubInfoForSelectResponseDto;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import io.swagger.annotations.ApiParam;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@ApiModel(value = "프로젝트 생성을 위해 필요한 정보", description = "호스트가 가진 클럽 정보 Response Dto Class")
@Getter
@Builder
@AllArgsConstructor
public class ProjectInfoForCreateResponseDto {

    @ApiModelProperty(example = "[{\"id\": 1, \"name\": \"클럽1\"}, {\"id\": 2, \"name\": \"클럽2\"}]")
    @ApiParam(value = "프로젝트를 생성하려는 멤버의 클럽 id, name 정보 리스트")
    private List<ClubInfoForSelectResponseDto> clubs;

    public static ProjectInfoForCreateResponseDto from(
        List<ClubInfoForSelectResponseDto> clubs) {
        return ProjectInfoForCreateResponseDto.builder()
            .clubs(clubs)
            .build();
    }
}
