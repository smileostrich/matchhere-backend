package com.mh.match.member.dto.response;


import com.mh.match.group.club.dto.response.ClubSimpleInfoResponseDto;
import com.mh.match.group.project.dto.response.ProjectSimpleInfoResponseDto;
import com.mh.match.group.study.dto.response.StudySimpleInfoResponseDto;
import lombok.Builder;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class MemberGroupResponseDto {
    private List<ProjectSimpleInfoResponseDto> projectList = new ArrayList<>();
    private List<StudySimpleInfoResponseDto> studyList = new ArrayList<>();
    private List<ClubSimpleInfoResponseDto> clubList = new ArrayList<>();

    public static MemberGroupResponseDto of(List<ProjectSimpleInfoResponseDto> projectList, List<StudySimpleInfoResponseDto> studyList, List<ClubSimpleInfoResponseDto> clubList) {
        return MemberGroupResponseDto.builder()
                .projectList(projectList)
                .studyList(studyList)
                .clubList(clubList)
                .build();
    }

    @Builder
    public MemberGroupResponseDto(List<ProjectSimpleInfoResponseDto> projectList, List<StudySimpleInfoResponseDto> studyList, List<ClubSimpleInfoResponseDto> clubList) {
        this.projectList = projectList;
        this.studyList = studyList;
        this.clubList = clubList;
    }
}
