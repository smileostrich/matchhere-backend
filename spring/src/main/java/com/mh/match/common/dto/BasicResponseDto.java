package com.mh.match.common.dto;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class BasicResponseDto {

    private Long id;

    public static BasicResponseDto from(Long id){
        return BasicResponseDto.builder()
            .id(id)
            .build();
    }
}
