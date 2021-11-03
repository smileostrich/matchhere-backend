package com.mh.match.group.project.entity;

import com.mh.match.common.entity.GroupCity;
import com.mh.match.common.entity.ProjectProgressState;
import com.mh.match.common.entity.PublicScope;
import com.mh.match.common.entity.RecruitmentState;
import com.mh.match.common.exception.CustomException;
import com.mh.match.common.exception.ErrorCode;
import com.mh.match.member.entity.Member;
import com.mh.match.s3.entity.DBFile;
import com.mh.match.group.club.entity.Club;
import com.mh.match.group.project.dto.request.ProjectCreateRequestDto;
import com.mh.match.group.project.dto.request.ProjectUpdateRequestDto;

import java.time.LocalDate;
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
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Entity(name = "matching.project")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Project {

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
    @Column(name = "project_progress_state", nullable = false)
    private ProjectProgressState projectProgressState;
    @Enumerated(EnumType.STRING)
    @Column(name = "public_scope", nullable = false)
    private PublicScope publicScope;
    @Enumerated(EnumType.STRING)
    @Column(name = "recruitment_state", nullable = false)
    private RecruitmentState recruitmentState;
    @Column(name = "view_count", nullable = false)
    private int viewCount;
    @Column(name = "create_date", nullable = false)
    private LocalDateTime createDate;
    @Column(name = "modify_date", nullable = false)
    private LocalDateTime modifyDate;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "host_id")
    private Member member;
    private String schedule;
    private LocalDate period;

    @Column(name = "developer_count", nullable = false)
    private int developerCount;
    @Column(name = "developer_max_count", nullable = false)
    private int developerMaxCount;
    @Column(name = "apply_developer", nullable = false)
    private Boolean applyDeveloper;

    @Column(name = "planner_count", nullable = false)
    private int plannerCount;
    @Column(name = "planner_max_count", nullable = false)
    private int plannerMaxCount;
    @Column(name = "apply_planner", nullable = false)
    private Boolean applyPlanner;

    @Column(name = "designer_count", nullable = false)
    private int designerCount;
    @Column(name = "designer_max_count", nullable = false)
    private int designerMaxCount;
    @Column(name = "apply_designer", nullable = false)
    private Boolean applyDesigner;

    @Enumerated(EnumType.STRING)
    private GroupCity city;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "club_id")
    private Club club;
    private String bio;
    @Column(name = "is_active", nullable = false)
    private Boolean isActive;

    public void plusViewCount(){
        this.viewCount++;
    }
    public void plusDeveloper() {
        if(Boolean.FALSE.equals(this.applyDeveloper)){
            throw new CustomException(ErrorCode.DEVELOPER_COUNT_OVER);
        }
        if(++this.developerCount == this.developerMaxCount){
            this.applyDeveloper = false;
        }
    }

    public void plusPlanner() {
        if(Boolean.FALSE.equals(this.applyPlanner)){
            throw new CustomException(ErrorCode.PLANNER_COUNT_OVER);
        }
        if(++this.plannerCount == this.plannerMaxCount){
            this.applyPlanner = false;
        }
    }

    public void plusDesigner() {
        if(Boolean.FALSE.equals(this.applyDesigner)){
            throw new CustomException(ErrorCode.DESIGNER_COUNT_OVER);
        }
        if(++this.designerCount == this.designerMaxCount){
            this.applyDesigner = false;
        }
    }

    public void minusDeveloper() {
        if(this.developerCount == 0){
            throw new CustomException(ErrorCode.DEVELOPER_COUNT_BELOW_ZERO);
        }
        if(--this.developerCount < this.developerMaxCount){
            this.applyDeveloper = true;
        }
    }

    public void minusPlanner() {
        if(this.plannerCount == 0){
            throw new CustomException(ErrorCode.PLANNER_COUNT_BELOW_ZERO);
        }
        if(--this.plannerCount < this.plannerMaxCount){
            this.applyPlanner = true;
        }
    }

    public void minusDesigner() {
        if(this.designerCount == 0){
            throw new CustomException(ErrorCode.DESIGNER_COUNT_BELOW_ZERO);
        }
        if(--this.designerCount < this.designerMaxCount){
            this.applyDesigner = true;
        }
    }

    public void setDeveloperMaxCount(int count){
        if(this.developerCount > count){
            throw new CustomException(ErrorCode.DEVELOPER_COUNT_MORE_THAN_MAX);
        }
        if(this.developerCount == count) {
            this.applyDeveloper = false;
        }
        if(this.developerCount < count) {
            this.developerMaxCount = count;
            this.applyDeveloper = true;
        }
    }

    public void setPlannerMaxCount(int count){
        if(this.plannerCount > count){
            throw new CustomException(ErrorCode.PLANNER_COUNT_MORE_THAN_MAX);
        }
        if(this.plannerCount == count){
            this.applyPlanner = false;
        }
        if(this.plannerCount < count) {
            this.plannerMaxCount = count;
            this.applyPlanner = true;
        }
    }

    public void setDesignerMaxCount(int count){
        if(this.designerCount > count){
            throw new CustomException(ErrorCode.DESIGNER_COUNT_MORE_THAN_MAX);
        }
        if(this.designerCount == count){
            this.applyDesigner = false;
        }
        if(this.designerCount < count) {
            this.designerMaxCount = count;
            this.applyDesigner = true;
        }
    }

    public void addRole(String str){
        if(str.equals("기획자")){
            this.plusPlanner();
        }else if(str.equals("개발자")){
            this.plusDeveloper();
        }else if(str.equals("디자이너")){
            this.plusDesigner();
        }else{
            throw new CustomException(ErrorCode.ROLE_NOT_FOUND);
        }

    }

    public void setCoverPic(DBFile coverPic) {
        this.coverPic = coverPic;
    }

    public void setMember(Member member){
        this.member = member;
    }

    public void removeRole(String str){
        if(str.equals("기획자")){
            this.minusPlanner();
        }else if(str.equals("개발자")){
            this.minusDeveloper();
        }else if(str.equals("디자이너")){
            this.minusDesigner();
        }else{
            throw new CustomException(ErrorCode.ROLE_NOT_FOUND);
        }
    }

    public void setIsActive(Boolean active){
        this.isActive = active;
    }

    public void initialCoverPic(){
        this.coverPic = null;
    }

    public void removeClub(){
        this.club = null;
    }

    public void update(ProjectUpdateRequestDto dto, Club club) {
        this.name = dto.getName();
        this.projectProgressState = ProjectProgressState.from(dto.getProjectProgressState());
        this.publicScope = PublicScope.from(dto.getPublicScope());
        this.recruitmentState = RecruitmentState.from(dto.getRecruitmentState());
        this.modifyDate = LocalDateTime.now();
        this.schedule = dto.getSchedule();
        this.period = dto.getPeriod();
        setDeveloperMaxCount(dto.getDeveloperMaxCount());
        setPlannerMaxCount(dto.getPlannerMaxCount());
        setDesignerMaxCount(dto.getDesignerMaxCount());
        this.city = GroupCity.from(dto.getCity());
        this.club = club;
        this.bio = dto.getBio();
    }

    public Project(ProjectCreateRequestDto dto, Club club, Member member) {
//        this.coverPic = coverPic;
        this.name = dto.getName();
        this.projectProgressState = ProjectProgressState.from(dto.getProjectProgressState());
        this.publicScope = PublicScope.from(dto.getPublicScope());
        this.recruitmentState = RecruitmentState.from(dto.getRecruitmentState());
        this.viewCount = 0;
        this.createDate = LocalDateTime.now();
        this.modifyDate = LocalDateTime.now();
        this.member = member;
        this.schedule = dto.getSchedule();
        this.period = dto.getPeriod();
        this.developerCount = 0;
        this.developerMaxCount = dto.getDeveloperMaxCount();
        this.applyDeveloper = dto.getDeveloperMaxCount() != 0;
        this.plannerCount = 0;
        this.plannerMaxCount = dto.getPlannerMaxCount();
        this.applyPlanner = (dto.getPlannerMaxCount() != 0);
        this.designerCount = 0;
        this.designerMaxCount = dto.getDesignerMaxCount();
        this.applyDesigner = dto.getDesignerMaxCount() != 0;
        this.city = GroupCity.from(dto.getCity());
        this.club = club;
        this.bio = dto.getBio();
        this.isActive = true;
    }
}