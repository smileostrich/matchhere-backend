package com.mh.match.group.club.dto.response;

import com.mh.match.group.club.entity.Club;
import com.mh.match.member.dto.MemberSimpleInfoResponseDto;
import com.mh.match.s3.dto.DBFileDto;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import io.swagger.annotations.ApiParam;
import java.time.LocalDateTime;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@ApiModel(value = "클럽 조회 정보", description = "클럽의 상세 정보 Response Dto Class")
@Getter
@Builder
@AllArgsConstructor
public class ClubInfoResponseDto {

    @ApiModelProperty(example = "4")
    private Long id;

    @ApiModelProperty(example = "["
            + "\"id\" : 97534f05-7e7f-425d-ac3e-aae8acee8a42, "
            + "\"file_name\" : \"썸네일\", "
            + "\"file_type\" : \"img\", "
            + "\"download_uri\" : \"http://localhost:8080/api/downloadFile/97534f05-7e7f-425d-ac3e-aae8acee8a42\"]")
    @ApiParam(value = "클럽 썸네일 정보")
    private DBFileDto coverPic;

    @ApiModelProperty(example = "알고리즘 클럽")
    private String name;

    @ApiModelProperty(example = "모집 중")
    @ApiParam(value = "모집 상태")
    private String recruitmentState;

    @ApiModelProperty(example = "32321")
    @ApiParam(value = "조회수")
    private int viewCount;

    @ApiModelProperty(example = "2021-09-06 06:57:37.667537")
    @ApiParam(value = "생성일자")
    private LocalDateTime createdDate;

    @ApiModelProperty(example = "[\"id\": 3, \"name\": \"박범진\", \"nickname\": \"BJP\"]")
    @ApiParam(value = "클럽장 정보(id, name, nickname)")
    private MemberSimpleInfoResponseDto host;

    @ApiModelProperty(example = "[{\"name\" : \"OS\"}, {\"name\" : \"DB\"}]")
    @ApiParam(value = "클럽 주제 정보")
    private List<ClubTopicResponseDto> topics;

    @ApiModelProperty(example = "3")
    @ApiParam(value = "클럽 현재 인원")
    private int memberCount;

    @ApiModelProperty(example = "3")
    private int maxCount;

    @ApiModelProperty(example =
            "[{\"id\": 4, \"name\": \"박범진\", \"nickname\": \"BJP\", \"email\": \"qjawls@naver.com\", \"coverPicUri\": \"http://localhost:8080/api/downloadFile/ac3e-aae8acee8a42\"},"
                    + " {\"id\": 5, \"name\": \"문일민\", \"nickname\": \"IMM\", \"email\": \"IMM@naver.com\", \"coverPicUri\": \"http://localhost:8080/api/downloadFile/97534f05-7e7f\"}]")
    @ApiParam(value = "해당 클럽에 속한 인원정보")
    private List<MemberSimpleInfoResponseDto> members;

    @ApiModelProperty(example = "알고리즘 클럽입니다.")
    private String bio;

    @ApiModelProperty(example = "소유자")
    @ApiParam(value = "조회한 사람의 권한")
    private String authority;

    public static ClubInfoResponseDto of(Club club, List<ClubTopicResponseDto> topics,
                                         List<MemberSimpleInfoResponseDto> members,
                                         String authority) {
        return ClubInfoResponseDto.builder()
                .id(club.getId())
                .coverPic(DBFileDto.of(club.getCoverPic()))
                .name(club.getName())
                .recruitmentState(club.getRecruitmentState().getState())
                .viewCount(club.getViewCount())
                .createdDate(club.getCreatedDate())
                .host(MemberSimpleInfoResponseDto.from(club.getMember()))
                .topics(topics)
                .memberCount(club.getMemberCount())
                .maxCount(club.getMaxCount())
                .members(members)
                .bio(club.getBio())
                .authority(authority)
                .build();
    }

}