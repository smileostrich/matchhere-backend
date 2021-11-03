package com.mh.match.group.study.dto.response;

import com.mh.match.group.study.entity.StudyApplicationForm;
import io.swagger.annotations.ApiModelProperty;
import io.swagger.annotations.ApiParam;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@AllArgsConstructor
public class StudyFormInfoResponseDto {

    private Long studyId;

    @ApiModelProperty(example = "3")
    @ApiParam(value = "멤버 id")
    private Long memberId;

    @ApiModelProperty(example = "박범진")
    @ApiParam(value = "신청자 이름")
    private String name;

    @ApiModelProperty(example = "설명란")
    @ApiParam(value = "자기 소개")
    private String bio;

    public static StudyFormInfoResponseDto from(StudyApplicationForm form){
        return StudyFormInfoResponseDto.builder()
            .studyId(form.getCompositeMemberStudy().getStudy().getId())
            .memberId(form.getCompositeMemberStudy().getMember().getId())
            .name(form.getName())
            .bio(form.getBio())
            .build();
    }

}
