package com.mh.match.member.dto.response;

import com.mh.match.member.entity.Member;
import com.mh.match.member.entity.MemberSns;
import io.swagger.annotations.ApiModelProperty;
import lombok.Builder;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;


@Data
public class MemberSnsResponseDto {
    @ApiModelProperty(name = "snsList", example = "[{\"id\":1, \"snsName\":\"github\", \"snsAccount\":\"gitid\"},{\"id\":2, \"snsName\":\"twitter\", \"snsAccount\":\"twitterid\"}]")
    private List<MemberSns> snsList = new ArrayList<>();

    public static MemberSnsResponseDto of(Member member, List<MemberSns> snsList) {
        return MemberSnsResponseDto.builder()
                .snsList(snsList)
                .build();
    }

    @Builder
    public MemberSnsResponseDto(List<MemberSns> snsList) {
        this.snsList = snsList;
    }
}
