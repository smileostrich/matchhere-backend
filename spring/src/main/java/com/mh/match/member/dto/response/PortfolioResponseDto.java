package com.mh.match.member.dto.response;


import com.mh.match.member.entity.Member;
import com.mh.match.s3.dto.DBFileDto;
import com.mh.match.s3.entity.DBFile;
import io.swagger.annotations.ApiModelProperty;
import lombok.Builder;
import lombok.Data;

@Data
public class PortfolioResponseDto {
    private DBFileDto portfolio;

    @ApiModelProperty(name = "portfolio_uri", example = "https://naver.com")
    private String portfolio_uri;

    public static PortfolioResponseDto of(Member member) {
        return PortfolioResponseDto.builder()
                .portfolio(DBFileDto.of(member.getPortfolio()))
                .portfolio_uri(member.getPortfolio_uri())
                .build();
    }

    @Builder
    public PortfolioResponseDto(DBFileDto portfolio, String portfolio_uri) {
        this.portfolio = portfolio;
        this.portfolio_uri = portfolio_uri;
    }
}
