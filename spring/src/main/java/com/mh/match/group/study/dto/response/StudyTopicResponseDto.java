package com.mh.match.group.study.dto.response;

import com.mh.match.group.study.entity.StudyTopic;
import io.swagger.annotations.ApiModelProperty;
import io.swagger.annotations.ApiParam;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@AllArgsConstructor
public class StudyTopicResponseDto {

    @ApiModelProperty(example = "OS")
    @ApiParam(value = "주제명")
    private String name;

    public static StudyTopicResponseDto from(StudyTopic studyTopic){
        return StudyTopicResponseDto.builder()
            .name(studyTopic.getName())
            .build();
    }

}
