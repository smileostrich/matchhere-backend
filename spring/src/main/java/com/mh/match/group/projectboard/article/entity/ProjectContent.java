package com.mh.match.group.projectboard.article.entity;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.validation.constraints.NotBlank;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@Entity(name = "matching.project_content")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class ProjectContent {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

//    @JsonIgnore
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "project_article_id")
    private ProjectArticle projectArticle;

    @NotBlank
    private String content;

    public void setContent(String content){
        this.content = content;
    }

    public static ProjectContent of(ProjectArticle projectArticle, String content) {
        return ProjectContent.builder()
            .projectArticle(projectArticle)
            .content(content)
            .build();
    }

}
