package com.mh.match.member.dto.request;


import com.mh.match.common.annotation.Enum;
import com.mh.match.common.entity.Position;
import io.swagger.annotations.ApiModelProperty;
import io.swagger.annotations.ApiParam;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotEmpty;
import java.util.HashMap;
import java.util.List;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class MemberSkillRequestDto {

    @ApiModelProperty(name = "position", example = "개발자")
    @ApiParam(value = "포지션", required = true)
    @Enum(enumClass = Position.class, ignoreCase = false)
    @NotEmpty
    private String position;

    @ApiModelProperty(name = "dpositionList", example = "[\"프론트엔드\", \"데브옵스\"]")
    @ApiParam(value = "세부 포지션", required = false)
    private List<String> dpositionList;

    @ApiModelProperty(name = "techList", example = "{\"python\":\"상\", \"java\":\"중\"}")
    @ApiParam(value = "기술스택 리스트", required = false)
    private HashMap<String,String> techList;
}
