package com.mh.match.group.club.dto.response;

import com.mh.match.group.club.entity.ClubTopic;
import io.swagger.annotations.ApiModelProperty;
import io.swagger.annotations.ApiParam;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@AllArgsConstructor
public class ClubTopicResponseDto {

    @ApiModelProperty(example = "취준")
    @ApiParam(value = "주제명")
    private String name;

    public static ClubTopicResponseDto from(ClubTopic clubTopic){
        return ClubTopicResponseDto.builder()
            .name(clubTopic.getName())
            .build();
    }

}
