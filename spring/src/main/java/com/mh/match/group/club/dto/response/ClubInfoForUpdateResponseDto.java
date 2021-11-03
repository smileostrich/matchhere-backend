package com.mh.match.group.club.dto.response;

import com.mh.match.group.club.entity.Club;
import com.mh.match.s3.dto.DBFileDto;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import io.swagger.annotations.ApiParam;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@ApiModel(value = "클럽 수정 정보", description = "클럽의  수정을 위한 정보 Response Dto Class")
@Getter
@Builder
@AllArgsConstructor
public class ClubInfoForUpdateResponseDto {

    @ApiModelProperty(example = "3")
    @ApiParam(value = "클럽 Id")
    private Long id;

    @ApiModelProperty(example = "["
        + "\"id\" : 97534f05-7e7f-425d-ac3e-aae8acee8a42, "
        + "\"file_name\" : \"썸네일\", "
        + "\"file_type\" : \"img\", "
        + "\"download_uri\" : \"http://localhost:8080/api/downloadFile/97534f05-7e7f-425d-ac3e-aae8acee8a42\"]")
    @ApiParam(value = "클럽 썸네일 정보")
    private DBFileDto coverPic;

    @ApiModelProperty(example = "알고리즘 클럽")
    @ApiParam(value = "클럽명")
    private String name;

    @ApiModelProperty(example = "[{\"name\" : \"OS\"}, {\"name\" : \"DB\"}]")
    @ApiParam(value = "클럽 주제 정보")
    private List<ClubTopicResponseDto> topics;

    @ApiModelProperty(example = "3")
    @ApiParam(value = "현재 인원")
    private int memberCount;

    @ApiModelProperty(name = "maxCount", example = "3")
    private int maxCount;

    @ApiModelProperty(example = "Git 매칭 프로젝트입니다.")
    @ApiParam(value = "프로젝트 소개")
    private String bio;

    @ApiModelProperty(example = "클럽 멤버에게만 공개")
    @ApiParam(value = "공개 범위")
    private String publicScope;

    @ApiModelProperty(example = "모집 중")
    @ApiParam(value = "모집 상태")
    private String recruitmentState;

    public static ClubInfoForUpdateResponseDto of(Club club, List<ClubTopicResponseDto> topics){
        return ClubInfoForUpdateResponseDto.builder()
            .id(club.getId())
            .name(club.getName())
            .coverPic(DBFileDto.of(club.getCoverPic()))
            .topics(topics)
            .bio(club.getBio())
            .memberCount(club.getMemberCount())
            .maxCount(club.getMaxCount())
            .publicScope(club.getPublicScope().getState())
            .recruitmentState(club.getRecruitmentState().getState())
            .build();
    }
}
