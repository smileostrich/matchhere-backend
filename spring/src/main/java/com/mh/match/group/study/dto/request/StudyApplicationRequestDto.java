package com.mh.match.group.study.dto.request;

import io.swagger.annotations.ApiModelProperty;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class StudyApplicationRequestDto {

    @ApiModelProperty(name = "bio", example = "설명ㅇㅇㅇㅇ")
    private String bio;

}