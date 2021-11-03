package com.mh.match.group.study.dto.response;

import io.swagger.annotations.ApiModelProperty;
import java.util.List;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class InfoForApplyStudyFormResponseDto {

//    private String name;

    @ApiModelProperty(name = "nickname", example = "BJP")
    private String nickname;

    @ApiModelProperty(name = "city", example = "서울")
    private String city;

    @ApiModelProperty(name = "git", example = "BEOMKING")
    private String git;

    @ApiModelProperty(name = "twitter", example = "twitter.com")
    private String twitter;

    @ApiModelProperty(name = "facebook", example = "facebook.com")
    private String facebook;

    @ApiModelProperty(name = "backjoon", example = "qjawlsqjacks")
    private String backjoon;

    @ApiModelProperty(name = "bio", example = "설명ㅇㅇㅇㅇ")
    private String bio;

    @ApiModelProperty(name = "projectTechstack", example = "[\"java\", \"python\"]")
    private List<String> strong;

    @ApiModelProperty(name = "projectTechstack", example = "[\"java\", \"python\"]")
    private List<String> knowledgeable;

}
