package com.mh.match.portfolio.entity;


import com.mh.match.member.entity.Member;
import com.mh.match.portfolio.dto.request.PortfolioCreateRequestDto;
import com.mh.match.portfolio.dto.request.PortfolioUpdateRequestDto;
import com.mh.match.s3.entity.DBFile;

import java.time.LocalDate;
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
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@Entity(name = "matching.portfolio")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class Portfolio {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank
    @Size(min = 2, max = 100)
    private String name;

    @Column(name = "website")
    private String website;

    @Column(name = "googleplay")
    private String googleplay;

    @Column(name = "appstore")
    private String appstore;

    @Column(name = "sourcecode")
    private String sourcecode;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "cover_pic")
    private DBFile coverPic;

    @Column(name = "view_count", nullable = false)
    private int viewCount;

    @Column(name = "created_date", nullable = false)
    private LocalDateTime createdDate;

    @NotNull
    private LocalDate start_date;
    private LocalDate end_date;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "writer", nullable = false)
    private Member member;

    private String bio;

    public void plusViewCount() {
        this.viewCount++;
    }

    public void initialCoverPic(){
        this.coverPic = null;
    }

    public void setCoverPic(DBFile coverPic){
        this.coverPic = coverPic;
    }

    public void update(PortfolioUpdateRequestDto dto){
        this.name = dto.getName();
        this.bio = dto.getBio();
    }

    public static Portfolio of(PortfolioCreateRequestDto dto, Member member){
        return Portfolio.builder()
                .name(dto.getName())
                .viewCount(0)
                .createdDate(LocalDateTime.now())
                .member(member)
                .bio(dto.getBio())
                .start_date(dto.getStart_date())
                .end_date(dto.getEnd_date())
                .build();
    }

}