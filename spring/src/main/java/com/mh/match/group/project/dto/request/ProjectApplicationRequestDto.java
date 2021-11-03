package com.mh.match.group.project.dto.request;

import io.swagger.annotations.ApiModelProperty;
import javax.validation.constraints.NotBlank;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ProjectApplicationRequestDto {

    @ApiModelProperty(example = "개발자")
    @NotBlank
    private String role;

    @ApiModelProperty(example = "설명란입니다.")
    private String bio;

}