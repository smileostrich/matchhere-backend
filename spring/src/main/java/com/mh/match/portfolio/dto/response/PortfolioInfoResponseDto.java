package com.mh.match.portfolio.dto.response;

import com.mh.match.member.dto.MemberSimpleInfoResponseDto;
import com.mh.match.portfolio.entity.Portfolio;
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
public class PortfolioInfoResponseDto {

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
    private List<PortfolioTopicResponseDto> topics;

    @ApiModelProperty(example = "알고리즘 클럽입니다.")
    private String bio;

    public static PortfolioInfoResponseDto of(Portfolio portfolio, List<PortfolioTopicResponseDto> topics) {
        return PortfolioInfoResponseDto.builder()
                .id(portfolio.getId())
                .coverPic(DBFileDto.of(portfolio.getCoverPic()))
                .name(portfolio.getName())
                .viewCount(portfolio.getViewCount())
                .createdDate(portfolio.getCreatedDate())
                .host(MemberSimpleInfoResponseDto.from(portfolio.getMember()))
                .topics(topics)
                .bio(portfolio.getBio())
                .build();
    }

}