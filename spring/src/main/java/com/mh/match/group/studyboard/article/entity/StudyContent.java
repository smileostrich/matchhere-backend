package com.mh.match.group.studyboard.article.entity;


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
@Entity(name = "matching.study_content")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class StudyContent {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "study_article_id")
    private StudyArticle studyArticle;

    @NotBlank
    private String content;

    public void setContent(String content){
        this.content = content;
    }

    public static StudyContent of(StudyArticle studyArticle, String content) {
        return StudyContent.builder()
            .studyArticle(studyArticle)
            .content(content)
            .build();
    }

}
