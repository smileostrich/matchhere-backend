package com.mh.match.group.club.dto.response;

import com.mh.match.group.club.entity.ClubApplicationForm;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import io.swagger.annotations.ApiParam;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@AllArgsConstructor
@ApiModel(value = "클럽 신청서 조회 정보", description = "클럽 신청서 조회 정보 Response Dto Class")
public class ClubFormInfoResponseDto {

    private Long clubId;

    @ApiModelProperty(example = "3")
    @ApiParam(value = "멤버 id")
    private Long memberId;

    @ApiModelProperty(example = "박범진")
    @ApiParam(value = "신청자 이름")
    private String name;

    @ApiModelProperty(example = "설명란")
    @ApiParam(value = "자기 소개")
    private String bio;

    public static ClubFormInfoResponseDto from(ClubApplicationForm form){
        return ClubFormInfoResponseDto.builder()
                .clubId(form.getCompositeMemberClub().getClub().getId())
                .memberId(form.getCompositeMemberClub().getMember().getId())
                .name(form.getName())
                .bio(form.getBio())
                .build();
    }
}