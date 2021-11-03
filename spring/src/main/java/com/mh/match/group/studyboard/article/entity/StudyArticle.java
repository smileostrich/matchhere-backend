package com.mh.match.group.studyboard.article.entity;

import com.mh.match.member.entity.Member;
import com.mh.match.group.studyboard.article.dto.StudyArticleRequestDto;
import com.mh.match.group.studyboard.board.entity.StudyBoard;

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
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@Builder
@Entity(name = "matching.study_article")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class StudyArticle {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "study_board_id")
    private StudyBoard studyBoard;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Member member;

    @NotEmpty
    @Column(name = "title")
    private String title;

    @Column(name = "create_date", nullable = false)
    private LocalDateTime createDate;

    @Column(name = "modified_date", nullable = false)
    private LocalDateTime modifiedDate;

    @Column(name = "view_count", nullable = false)
    private int viewCount;

    public void plusViewCount(){
        this.viewCount++;
    }

    public void update(StudyArticleRequestDto dto, StudyBoard studyBoard){
        this.studyBoard = studyBoard;
        this.title = dto.getTitle();
        this.modifiedDate = LocalDateTime.now();
    }

    public static StudyArticle of(StudyArticleRequestDto dto, StudyBoard studyBoard, Member member){
        return StudyArticle.builder()
            .studyBoard(studyBoard)
            .member(member)
            .title(dto.getTitle())
            .createDate(LocalDateTime.now())
            .modifiedDate(LocalDateTime.now())
            .viewCount(0)
            .build();
    }
}
