package com.mh.match.portfolio.dto.response;

import com.mh.match.portfolio.entity.Portfolio;
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
public class PortfolioInfoForUpdateResponseDto {

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
    private List<PortfolioTagResponseDto> topics;

    @ApiModelProperty(example = "Git 매칭 프로젝트입니다.")
    @ApiParam(value = "프로젝트 소개")
    private String bio;

    public static PortfolioInfoForUpdateResponseDto of(Portfolio portfolio, List<PortfolioTagResponseDto> topics){
        return PortfolioInfoForUpdateResponseDto.builder()
            .id(portfolio.getId())
            .name(portfolio.getName())
            .coverPic(DBFileDto.of(portfolio.getCoverPic()))
            .topics(topics)
            .bio(portfolio.getBio())
            .build();
    }
}
