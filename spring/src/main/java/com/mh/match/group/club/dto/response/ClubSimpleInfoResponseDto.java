package com.mh.match.group.club.dto.response;

import com.mh.match.group.club.entity.Club;
import com.mh.match.member.dto.MemberSimpleInfoResponseDto;
import io.swagger.annotations.ApiModelProperty;
import io.swagger.annotations.ApiParam;
import java.time.LocalDateTime;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@AllArgsConstructor
public class ClubSimpleInfoResponseDto {

    @ApiModelProperty(example = "3")
    @ApiParam(value = "클럽 Id")
    private Long id;

    @ApiModelProperty(example = "http://localhost:8080/api/downloadFile/97534f05-7e7f-425d-ac3e-aae8acee8a42")
    @ApiParam(value = "파일 다운로드 Uri")
    private String coverPicUri;

    @ApiModelProperty(example = "알고리즘 클럽")
    @ApiParam(value = "클럽명")
    private String name;

    @ApiModelProperty(example = "모집 중")
    @ApiParam(value = "모집 상태")
    private String recruitmentState;

    @ApiModelProperty(example = "32321")
    @ApiParam(value = "조회수")
    private int viewCount;

    @ApiModelProperty(example = "2021-09-06 06:57:37.667537")
    @ApiParam(value = "생성일자")
    private LocalDateTime createDate;

    @ApiModelProperty(example = "[\"id\": 3, \"name\": \"박범진\", \"nickname\": \"BJP\", \"coverPicUri\": \"http://localhost:8080/api/downloadFile/97534f05-7e7f-425d-ac3e-aae8acee8a42\"]")
    @ApiParam(value = "클럽장 정보(id, name, nickname, coverPicUri)")
    private MemberSimpleInfoResponseDto host;

    @ApiModelProperty(example = "[{\"name\" : \"OS\"}, {\"name\" : \"DB\"}]")
    @ApiParam(value = "클럽 주제 정보")
    private List<ClubTopicResponseDto> topics;

    public static ClubSimpleInfoResponseDto of(Club club, List<ClubTopicResponseDto> topics) {
        return ClubSimpleInfoResponseDto.builder()
            .id(club.getId())
            .name(club.getName())
            .coverPicUri(
                (club.getCoverPic() == null) ? null : club.getCoverPic().getDownload_uri())
            .recruitmentState(club.getRecruitmentState().getState())
            .viewCount(club.getViewCount())
            .createDate(club.getCreatedDate())
            .host(MemberSimpleInfoResponseDto.from(club.getMember()))
            .topics(topics)
            .build();
    }

}
