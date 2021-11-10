package com.mh.match.mogakco.comment.entity;

import com.mh.match.member.entity.Member;
import com.mh.match.mogakco.article.entity.MogakcoArticle;
import com.mh.match.mogakco.comment.dto.MogakcoArticleCommentRequestDto;
import lombok.*;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.time.LocalDateTime;

@Getter
@Setter
@Builder
@Entity(name = "matching.mogakco_article_comment")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class MogakcoArticleComment {

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

    @Column(name = "reply_count", nullable = false)
    private int replyCount;
    @Column(name = "is_modified", nullable = false)
    private Boolean isModified;
    @Column(name = "is_deleted", nullable = false)
    private Boolean isDeleted;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "mogakco_article_id")
    private MogakcoArticle mogakcoArticle;

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

    public static MogakcoArticleComment of(MogakcoArticleCommentRequestDto dto, Member member, MogakcoArticle mogakcoArticle){
        return MogakcoArticleComment.builder()
                .member(member)
                .mogakcoArticle(mogakcoArticle)
                .content(dto.getContent())
                .createDate(LocalDateTime.now())
                .modifiedDate(LocalDateTime.now())
                .isModified(false)
                .isDeleted(false)
                .replyCount(0)
                .build();
    }

}