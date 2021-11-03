package com.mh.match.group.study.entity;

import com.mh.match.common.entity.GroupCity;
import com.mh.match.common.entity.PublicScope;
import com.mh.match.common.entity.RecruitmentState;
import com.mh.match.common.entity.StudyProgressState;
import com.mh.match.common.exception.CustomException;
import com.mh.match.common.exception.ErrorCode;
import com.mh.match.member.entity.Member;
import com.mh.match.s3.entity.DBFile;
import com.mh.match.group.club.entity.Club;
import com.mh.match.group.study.dto.request.StudyCreateRequestDto;
import com.mh.match.group.study.dto.request.StudyUpdateRequestDto;

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
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Entity(name = "matching.study")
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class Study {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank
    @Size(min = 2, max = 100)
    private String name;

    @OneToOne
    @JoinColumn(name = "cover_pic")
    private DBFile coverPic;

    @Enumerated(EnumType.STRING)
    @Column(name = "study_progress_state", nullable = false)
    private StudyProgressState studyProgressState;

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

    @Column(name = "modified_date", nullable = false)
    private LocalDateTime modifiedDate;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "host_id", nullable = false)
    private Member member;

    private String schedule;

    @Column(name = "member_count", nullable = false)
    private int memberCount;

    @Column(name = "max_count", nullable = false)
    @Min(1)
    private int maxCount;

    @Enumerated(EnumType.STRING)
    @NotNull
    private GroupCity city;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "club_id")
    private Club club;

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

    public void removeClub(){
        this.club = null;
    }

    public void update(StudyUpdateRequestDto dto, Club club){
        this.name = dto.getName();
        this.studyProgressState = StudyProgressState.from(dto.getStudyProgressState());
        this.publicScope = PublicScope.from(dto.getPublicScope());
        this.recruitmentState = RecruitmentState.from(dto.getRecruitmentState());
        this.modifiedDate = LocalDateTime.now();
        this.schedule = dto.getSchedule();
        setMaxCount(dto.getMaxCount());
        this.city = GroupCity.from(dto.getCity());
        this.club = club;
        this.bio = dto.getBio();
    }

    public static Study of(StudyCreateRequestDto dto, Club club, Member member){
        return Study.builder()
//            .coverPic(coverPic)
            .name(dto.getName())
            .studyProgressState(StudyProgressState.from(dto.getStudyProgressState()))
            .publicScope(PublicScope.from(dto.getPublicScope()))
            .recruitmentState(RecruitmentState.from(dto.getRecruitmentState()))
            .viewCount(0)
            .createdDate(LocalDateTime.now())
            .modifiedDate(LocalDateTime.now())
            .member(member)
            .schedule(dto.getSchedule())
            .memberCount(0)
            .maxCount(dto.getMaxCount())
            .city(GroupCity.from(dto.getCity()))
            .club(club)
            .bio(dto.getBio())
            .isActive(true)
            .build();
    }

}