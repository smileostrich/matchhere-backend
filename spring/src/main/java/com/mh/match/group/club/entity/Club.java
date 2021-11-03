package com.mh.match.group.club.entity;

import com.mh.match.common.entity.PublicScope;
import com.mh.match.common.entity.RecruitmentState;
import com.mh.match.common.exception.CustomException;
import com.mh.match.common.exception.ErrorCode;
import com.mh.match.member.entity.Member;
import com.mh.match.s3.entity.DBFile;
import com.mh.match.group.club.dto.request.ClubCreateRequestDto;
import com.mh.match.group.club.dto.request.ClubUpdateRequestDto;

import java.time.LocalDateTime;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@Entity(name = "matching.club")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class Club {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank
    @Size(min = 2, max = 100)
    private String name;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "cover_pic")
    private DBFile coverPic;

    @Enumerated(EnumType.STRING)
    @Column(name = "public_scope", nullable = false)
    private PublicScope publicScope;

    @Enumerated(EnumType.STRING)
    @Column(name = "recruitment_state", nullable = false)
    private RecruitmentState recruitmentState;

    @Column(name = "view_count", nullable = false)
    private int viewCount;

    @Column(name = "created_date", nullable = false)
    private LocalDateTime createdDate;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "host_id", nullable = false)
    private Member member;

    @Column(name = "member_count", nullable = false)
    private int memberCount;

    @Column(name = "max_count", nullable = false)
    @Min(1)
    private int maxCount;

    private String bio;

    @Column(name = "is_active", nullable = false)
    private Boolean isActive;

    public void plusViewCount() {
        this.viewCount++;
    }

    public void addMember() {
        if (this.memberCount >= this.maxCount) {
            throw new CustomException(ErrorCode.MEMBER_COUNT_OVER);
        }
        this.memberCount++;
    }

    public void removeMember() {
        if (this.memberCount <= 0) {
            throw new CustomException(ErrorCode.MEMBER_COUNT_BELOW_ZERO);
        }
        this.memberCount--;
    }

    public void setMember(Member member) {
        this.member = member;
    }

    public void setMaxCount(int count){
        if (this.memberCount > count || count < 1) {
            throw new CustomException(ErrorCode.MEMBER_COUNT_OVER);
        }
        this.maxCount = count;
    }

    public void activation(){
        this.isActive = true;
    }

    public void deActivation(){
        this.isActive = false;
    }

    public void initialCoverPic(){
        this.coverPic = null;
    }

    public void setCoverPic(DBFile coverPic){
        this.coverPic = coverPic;
    }

    public void update(ClubUpdateRequestDto dto){
        this.name = dto.getName();
        this.publicScope = PublicScope.from(dto.getPublicScope());
        this.recruitmentState = RecruitmentState.from(dto.getRecruitmentState());
        setMaxCount(dto.getMaxCount());
        this.bio = dto.getBio();
    }

    public static Club of(ClubCreateRequestDto dto, Member member){
        return Club.builder()
//                .coverPic(coverPic)
                .name(dto.getName())
                .publicScope(PublicScope.from(dto.getPublicScope()))
                .recruitmentState(RecruitmentState.from(dto.getRecruitmentState()))
                .viewCount(0)
                .createdDate(LocalDateTime.now())
                .member(member)
                .memberCount(0)
                .maxCount(dto.getMaxCount())
                .bio(dto.getBio())
                .isActive(true)
                .build();
    }

}