package com.mh.match.member.dto.request;


import io.swagger.annotations.ApiModelProperty;
import io.swagger.annotations.ApiParam;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.HashMap;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class MemberSnsRequestDto {
    @ApiModelProperty(name = "techList", example = "{\"github\":\"github_id\", \"facebook\":\"facebook_id\"}")
    @ApiParam(value = "기술스택 리스트", required = false)
    private HashMap<String,String> snsList;
}
