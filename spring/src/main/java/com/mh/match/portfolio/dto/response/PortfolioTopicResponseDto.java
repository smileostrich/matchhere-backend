package com.mh.match.portfolio.dto.response;

import com.mh.match.portfolio.entity.PortfolioTopic;
import io.swagger.annotations.ApiModelProperty;
import io.swagger.annotations.ApiParam;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@AllArgsConstructor
public class PortfolioTopicResponseDto {

    @ApiModelProperty(example = "취준")
    @ApiParam(value = "주제명")
    private String name;

    public static PortfolioTopicResponseDto from(PortfolioTopic portfolioTopic){
        return PortfolioTopicResponseDto.builder()
            .name(portfolioTopic.getName())
            .build();
    }

}
