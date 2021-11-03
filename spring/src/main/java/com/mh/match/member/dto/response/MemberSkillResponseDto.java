package com.mh.match.member.dto.response;


import com.mh.match.member.entity.Member;
import com.mh.match.common.dto.DetailPositionInterface;
import com.mh.match.member.dto.inter.MemberTechstackInterface;
import io.swagger.annotations.ApiModelProperty;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Data
public class MemberSkillResponseDto {

    @ApiModelProperty(name = "position", example = "개발자")
    private String position;

    @ApiModelProperty(name = "techList", example = "[{\"name\":\"python\", \"level\":\"상\", \"img_uri\":\"http://cdn.matchhere.me/path/python.png\"}, {\"name\":\"java\", \"level\":\"중\", \"img_uri\":\"http://cdn.matchhere.me/path/java.png\"}]")
    private List<MemberTechstackInterface> techList = new ArrayList<>();

    @ApiModelProperty(name = "dpositionList", example = "[{\"name\":\"프론트엔드\"}, {\"name\":\"데브옵스\"}]")
    private List<DetailPositionInterface> dpositionList = new ArrayList<>();

    public static MemberSkillResponseDto of(Member member, List<DetailPositionInterface> dpositionList, List<MemberTechstackInterface> techList) {
        return MemberSkillResponseDto.builder()
                .position(member.getPosition())
                .techList(techList)
                .dpositionList(dpositionList)
                .build();
    }

    @Builder
    public MemberSkillResponseDto(String position, List<MemberTechstackInterface> techList, List<DetailPositionInterface> dpositionList) {
        this.position = position;
        this.techList = techList;
        this.dpositionList = dpositionList;
    }
}
