package com.mh.match.group.club.dto.request;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import io.swagger.annotations.ApiParam;
import java.util.List;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@ApiModel(value = "클럽 업데이트 정보")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ClubUpdateRequestDto {

    @ApiModelProperty(example = "매칭 클럽")
    @ApiParam(value = "클럽명", required = true)
    @NotBlank
    @Size(min = 2, max = 100)
    private String name;

    @ApiModelProperty(example = "[\"인맥\", \"취준\"]")
    @ApiParam(value = "주제")
    private List<String> topics;

    @ApiModelProperty(example = "3")
    @ApiParam(value = "클럽 제한 인원", required = true)
    @NotNull
    private int maxCount;

    @ApiModelProperty(example = "알고리즘 클럽입니다.")
    @ApiParam(value = "클럽 소개", required = true)
    private String bio;

    @ApiModelProperty(example = "전체 공개")
    @ApiParam(value = "공개 범위", required = true)
    @NotBlank
    private String publicScope;

    @ApiModelProperty(example = "모집 중")
    @ApiParam(value = "모집 상태", required = true)
    @NotBlank
    private String recruitmentState;

}
