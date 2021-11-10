package com.mh.match.mogakco.article.entity;

import lombok.*;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;

@Getter
@Builder
@Entity(name = "matching.mogakco_article_tag")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class MogakcoArticleTag {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "mogakco_article_id", nullable = false)
    private MogakcoArticle mogakcoArticle;

    @NotBlank
    private String name;

    public static MogakcoArticleTag of(MogakcoArticle mogakcoArticle, String name){
        return MogakcoArticleTag.builder()
                .mogakcoArticle(mogakcoArticle)
                .name(name)
                .build();
    }

}
