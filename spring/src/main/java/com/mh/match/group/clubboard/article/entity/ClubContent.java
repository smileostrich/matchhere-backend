package com.mh.match.group.clubboard.article.entity;


import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.validation.constraints.NotBlank;

import lombok.*;

@Getter
@Builder
@Entity(name = "matching.club_content")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class ClubContent {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne(fetch = FetchType.LAZY, cascade = CascadeType.REMOVE, orphanRemoval = true)
    @JoinColumn(name = "club_article_id")
    private ClubArticle clubArticle;

    @NotBlank
    private String content;

    public void setContent(String content){
        this.content = content;
    }

    public static ClubContent of(ClubArticle clubArticle, String content) {
        return ClubContent.builder()
                .clubArticle(clubArticle)
                .content(content)
                .build();
    }

}
