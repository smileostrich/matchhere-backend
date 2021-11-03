package com.mh.match.member.dto.request;


import lombok.Getter;

@Getter
public class EmailCertRequestDto {
    private String email;
    private String authCode;
}
