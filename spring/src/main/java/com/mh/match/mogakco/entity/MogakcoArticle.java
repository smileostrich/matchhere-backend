package com.mh.match.mogakco.entity;

import com.mh.match.member.entity.Member;
import com.mh.match.mogakco.dto.MogakcoArticleRequestDto;
import lombok.*;

import javax.persistence.*;
import javax.validation.constraints.NotEmpty;
import java.time.LocalDateTime;

@Getter
@Setter
@Builder
@Entity(name = "matching.mogakco_article")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class MogakcoArticle {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

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

    public void update(MogakcoArticleRequestDto dto){
        this.title = dto.getTitle();
        this.modifiedDate = LocalDateTime.now();
    }

    public static MogakcoArticle of(MogakcoArticleRequestDto dto, Member member){
        return MogakcoArticle.builder()
                .member(member)
                .title(dto.getTitle())
                .createDate(LocalDateTime.now())
                .modifiedDate(LocalDateTime.now())
                .build();
    }
}