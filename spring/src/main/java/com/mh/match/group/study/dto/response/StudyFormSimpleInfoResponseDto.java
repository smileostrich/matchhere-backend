package com.mh.match.group.study.dto.response;

import com.mh.match.group.study.entity.StudyApplicationForm;
import com.mh.match.member.dto.MemberSimpleInfoResponseDto;
import io.swagger.annotations.ApiModelProperty;
import io.swagger.annotations.ApiParam;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@AllArgsConstructor
public class StudyFormSimpleInfoResponseDto {

    @ApiModelProperty(example = "4")
    @ApiParam(value = "스터디 id")
    private Long studyId;

    @ApiModelProperty(example = "[\"id\": 3, \"name\": \"박범진\", \"nickname\": \"BJP\", \"coverPicUri\":\"http://localhost:8080/api/downloadFile/97534f05-7e7f-425d-ac3e-aae8acee8a42\"]")
    @ApiParam(value = "작성자 정보(id, name, nickname, coverPicUri)")
    private MemberSimpleInfoResponseDto writer;

    public static StudyFormSimpleInfoResponseDto from(StudyApplicationForm form){
        return StudyFormSimpleInfoResponseDto.builder()
            .studyId(form.getCompositeMemberStudy().getStudy().getId())
            .writer(MemberSimpleInfoResponseDto.from(form.getCompositeMemberStudy().getMember()))
            .build();
    }
}
