package com.mh.match.group.studyboard.comment.entity;

import com.mh.match.group.studyboard.comment.dto.StudyArticleCommentRequestDto;
import com.mh.match.member.entity.Member;
import com.mh.match.group.studyboard.article.entity.StudyArticle;

import java.time.LocalDateTime;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@Builder
@Table(name = "study_article_comment")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class StudyArticleComment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank
    @Size(max = 500)
    private String content;

    @Column(name = "create_date", nullable = false)
    private LocalDateTime createDate;
    @Column(name = "modified_date", nullable = false)
    private LocalDateTime modifiedDate;

    @NotNull
    private int depth;
    @Column(name = "parent_id")
    private Long parentId;

//    @Column(name = "group_id") 뎁스가 1이 아니고 더 깊어진다면 필요
//    private Long groupId;

    @Column(name = "reply_count", nullable = false)
    private int replyCount;
    @Column(name = "is_modified", nullable = false)
    private Boolean isModified;
    @Column(name = "is_deleted", nullable = false)
    private Boolean isDeleted;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "study_article_id")
    private StudyArticle studyArticle;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Member member;

    public void addReplyCount(){
        replyCount++;
    }
    public void removeReplyCount(){
        replyCount--;
    }

    public void setDepth(Long parentId){
        if(parentId > 0){
            this.depth = 1;
        }else{
            this.depth = 0;
        }
        this.parentId = parentId;
    }

    public static StudyArticleComment of(StudyArticleCommentRequestDto dto, Member member, StudyArticle studyArticle){
        return StudyArticleComment.builder()
            .member(member)
            .studyArticle(studyArticle)
            .content(dto.getContent())
            .createDate(LocalDateTime.now())
            .modifiedDate(LocalDateTime.now())
            .isModified(false)
            .isDeleted(false)
            .replyCount(0)
            .build();
    }

}
