package com.mh.match.member.dto.response;


import com.mh.match.member.entity.Member;
import com.mh.match.member.entity.MemberSns;
import com.mh.match.s3.dto.DBFileDto;
import com.mh.match.s3.entity.DBFile;
import io.swagger.annotations.ApiModelProperty;
import lombok.Builder;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class MemberSnsPortfolioResponseDto {
    private DBFileDto portfolio;

    @ApiModelProperty(name = "portfolio_uri", example = "https://naver.com")
    private String portfolio_uri;

    @ApiModelProperty(name = "snsList", example = "[{\"id\":1, \"snsName\":\"github\", \"snsAccount\":\"gitid\"},{\"id\":2, \"snsName\":\"twitter\", \"snsAccount\":\"twitterid\"}]")
    private List<MemberSns> snsList = new ArrayList<>();

    public static MemberSnsPortfolioResponseDto of(Member member, List<MemberSns> snsList) {
        return MemberSnsPortfolioResponseDto.builder()
                .portfolio(DBFileDto.of(member.getPortfolio()))
                .portfolio_uri(member.getPortfolio_uri())
                .snsList(snsList)
                .build();
    }

    @Builder
    public MemberSnsPortfolioResponseDto(DBFileDto portfolio, String portfolio_uri, List<MemberSns> snsList) {
        this.portfolio = portfolio;
        this.portfolio_uri = portfolio_uri;
        this.snsList = snsList;
    }
}
