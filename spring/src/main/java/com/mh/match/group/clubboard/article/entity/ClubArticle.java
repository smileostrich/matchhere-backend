package com.mh.match.group.clubboard.article.entity;

import com.mh.match.member.entity.Member;
import com.mh.match.group.clubboard.article.dto.ClubArticleRequestDto;
import com.mh.match.group.clubboard.board.entity.ClubBoard;

import java.time.LocalDateTime;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.validation.constraints.NotEmpty;

import lombok.*;

@Getter
@Setter
@Builder
@Entity(name = "matching.club_article")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class ClubArticle {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "club_board_id")
    private ClubBoard clubBoard;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Member member;

    @NotEmpty
    @Column(name = "title")
    private String title;

    @Column(name = "create_date")
    private LocalDateTime createDate;

    @Column(name = "modified_date")
    private LocalDateTime modifiedDate;

    @Column(name = "view_count")
    private Integer viewCount;

    public void plusViewCount(){
        this.viewCount++;
    }

    public void update(ClubArticleRequestDto dto, ClubBoard clubBoard){
        this.clubBoard = clubBoard;
        this.title = dto.getTitle();
        this.modifiedDate = LocalDateTime.now();
    }

    public static ClubArticle of(ClubArticleRequestDto dto, ClubBoard clubBoard, Member member){
        return ClubArticle.builder()
                .clubBoard(clubBoard)
                .member(member)
                .title(dto.getTitle())
                .createDate(LocalDateTime.now())
                .modifiedDate(LocalDateTime.now())
                .viewCount(0)
                .build();
    }
}