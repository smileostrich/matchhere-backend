package com.mh.match.member.dto.request;


import com.fasterxml.jackson.annotation.JsonIgnore;
import com.mh.match.member.entity.Member;
import com.mh.match.common.annotation.Enum;
import com.mh.match.common.entity.City;
import io.swagger.annotations.ApiModelProperty;
import io.swagger.annotations.ApiParam;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Pattern;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class MemberBasicInfoRequestDto {
    @ApiModelProperty(name = "coverpic_uuid", example = "uuid")
    @ApiParam(value = "사용자 커버 사진 uuid", required = false)
    private String coverpic_uuid;

    @ApiModelProperty(name = "nickname", example = "별명")
    @ApiParam(value = "별명", required = true)
    @Pattern(regexp = "^[0-9a-zA-Z가-힣]{2,20}$")
    @NotEmpty
    private String nickname;

    @ApiModelProperty(name = "name", example = "문일민")
    @ApiParam(value = "이름", required = true)
    @Pattern(regexp = "^[가-힣]{2,30}|[a-zA-Z]{2,30}$")
    @NotEmpty
    private String name;

    @ApiModelProperty(name = "city", example = "부산")
    @ApiParam(value = "지역", required = true)
    @Enum(enumClass = City.class, ignoreCase = false)
    @NotEmpty
    private String city;

    @ApiModelProperty(name = "bio", example = "let me introduce")
    @ApiParam(value = "자기소개", required = false)
    private String bio;

    @JsonIgnore
    public void setMember(Member member) {
        member.setNickname(nickname);
        member.setName(name);
        member.setCity(city);
        member.setBio(bio);
    }
}
