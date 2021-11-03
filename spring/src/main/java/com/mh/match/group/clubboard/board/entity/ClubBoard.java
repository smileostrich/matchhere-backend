package com.mh.match.group.clubboard.board.entity;


import com.mh.match.group.club.entity.Club;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.validation.constraints.NotEmpty;

import com.mh.match.group.clubboard.board.dto.ClubBoardCreateRequestDto;
import lombok.*;

@Getter
@Builder
@Entity(name = "matching.club_board")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class ClubBoard {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "club_id")
    private Club club;

    @NotEmpty
    @Column(name = "name")
    private String name;

    public void setName(String name) {
        this.name = name;
    }

    public static ClubBoard of(ClubBoardCreateRequestDto dto, Club club){
        return ClubBoard.builder()
                .club(club)
                .name(dto.getName())
                .build();
    }

    public ClubBoard(String name, Club club) {
        this.name = name;
        this.club = club;
    }
}
