package com.mh.match.group.study.service;

import com.mh.match.group.study.dto.response.*;
import com.mh.match.member.entity.Member;
import com.mh.match.s3.dto.DBFileDto;
import com.mh.match.group.study.dto.request.StudyApplicationRequestDto;
import com.mh.match.group.study.dto.request.StudyCreateRequestDto;
import com.mh.match.group.study.dto.request.StudyUpdateRequestDto;
import com.mh.match.group.study.entity.Study;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.web.multipart.MultipartFile;

public interface StudyService {

    // 스터디 생성하려는 사람의 클럽 정보
    StudyInfoForCreateResponseDto getInfoForCreate();

    //스터디 생성
    StudyInfoResponseDto create(MultipartFile file, StudyCreateRequestDto dto);

    // 스터디 수정
    StudyInfoResponseDto update(Long studyId, StudyUpdateRequestDto dto);

    // 스터디 수정시 사진 변경
    DBFileDto changeCoverPic(MultipartFile file, Long studyId);

    DBFileDto getCoverPicUri(Long studyId);

    // 조회 수 증가
    HttpStatus plusViewCount(Long studyId);

    // 권한 변경
    HttpStatus changeAuthority(Long studyId, Long memberId, String authority);

    HttpStatus delete(Long studyId);

    // 스터디 전체 조회
    Page<StudySimpleInfoResponseDto> getAllStudy(Pageable pageable);

    // 스터디 상세 조회
    StudyInfoResponseDto getOneStudy(Long studyId);

    // 현재 스터디 간편 정보 리턴
    StudySimpleInfoResponseDto getOneSimpleStudy(Long studyId);

    String getMemberAuthority(Long studyId);

    // 스터디 구성원 리스트
    List<StudyMemberResponseDto> getMembersInStudy(Long studyId);

    StudyInfoForUpdateResponseDto getInfoForUpdateStudy(Long studyId);

    void addMember(Study study, Member member);

    HttpStatus removeMe(Long studyId);

    HttpStatus removeMember(Long studyId, Long memberId);

    boolean checkCanApply(Long studyId);

    StudyFormInfoResponseDto applyStudy(Long studyId, StudyApplicationRequestDto dto);

    // 모든 스터디 신청서 조회
    List<StudyFormSimpleInfoResponseDto> getAllStudyForm(Long studyId);

//    List<StudyFormInfoResponseDto> getAllFormByStudyNickname(Long studyId, String nickname);

    StudyFormInfoResponseDto getOneStudyForm(Long studyId, Long memberId);

    HttpStatus approval(Long studyId, Long memberId);

    HttpStatus reject(Long studyId, Long memberId);

}
