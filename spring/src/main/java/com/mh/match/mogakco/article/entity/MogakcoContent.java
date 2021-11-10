package com.mh.match.mogakco.article.entity;


import lombok.*;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;

@Getter
@Builder
@Entity(name = "matching.mogakco_content")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class MogakcoContent {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne(fetch = FetchType.LAZY, cascade = CascadeType.REMOVE, orphanRemoval = true)
    @JoinColumn(name = "mogakco_article_id")
    private MogakcoArticle mogakcoArticle;

    @NotBlank
    private String content;

    public void setContent(String content){
        this.content = content;
    }

    public static MogakcoContent of(MogakcoArticle mogakcoArticle, String content) {
        return MogakcoContent.builder()
                .mogakcoArticle(mogakcoArticle)
                .content(content)
                .build();
    }

}
