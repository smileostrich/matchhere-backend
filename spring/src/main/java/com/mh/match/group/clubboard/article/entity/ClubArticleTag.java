package com.mh.match.group.clubboard.article.entity;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;

import lombok.*;

@Getter
@Builder
@Entity(name = "matching.club_article_tag")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class ClubArticleTag {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "club_article_id", nullable = false)
    private ClubArticle clubArticle;

    @NotBlank
    private String name;

    public static ClubArticleTag of(ClubArticle clubArticle, String name){
        return ClubArticleTag.builder()
                .clubArticle(clubArticle)
                .name(name)
                .build();
    }

}
