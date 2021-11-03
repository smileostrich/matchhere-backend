package com.mh.match.invite.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class InviteLinkResponseDto {

    @ApiModelProperty(example = "http://localhost:8080/api/project/4")
    private String inviteLinkUri;

    public static InviteLinkResponseDto from(String uri){
        return new InviteLinkResponseDto(uri);
    }
}
