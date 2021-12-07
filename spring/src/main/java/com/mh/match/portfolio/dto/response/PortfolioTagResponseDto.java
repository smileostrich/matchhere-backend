package com.mh.match.portfolio.dto.response;

import com.mh.match.portfolio.entity.PortfolioTag;
import io.swagger.annotations.ApiModelProperty;
import io.swagger.annotations.ApiParam;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@AllArgsConstructor
public class PortfolioTagResponseDto {

    @ApiModelProperty(example = "취준")
    @ApiParam(value = "주제명")
    private String name;

    public static PortfolioTagResponseDto from(PortfolioTag portfolioTag){
        return PortfolioTagResponseDto.builder()
            .name(portfolioTag.getName())
            .build();
    }

}
