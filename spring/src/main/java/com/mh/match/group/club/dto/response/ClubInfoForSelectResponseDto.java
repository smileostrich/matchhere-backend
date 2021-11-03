package com.mh.match.group.club.dto.response;

import com.mh.match.group.club.entity.Club;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@AllArgsConstructor
public class ClubInfoForSelectResponseDto {

    private Long id;

    private String name;

    public static ClubInfoForSelectResponseDto from(Club club) {
        return ClubInfoForSelectResponseDto.builder()
            .id(club.getId())
            .name(club.getName())
            .build();
    }
}
