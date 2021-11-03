package com.mh.match.group.club.service;

import com.mh.match.group.club.dto.response.*;
import com.mh.match.member.entity.Member;
import com.mh.match.s3.dto.DBFileDto;
import com.mh.match.group.club.dto.request.ClubApplicationRequestDto;
import com.mh.match.group.club.dto.request.ClubCreateRequestDto;
import com.mh.match.group.club.dto.request.ClubUpdateRequestDto;
import com.mh.match.group.club.dto.response.*;
import com.mh.match.group.club.entity.Club;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.web.multipart.MultipartFile;

public interface ClubService {

    //클럽 생성
    ClubInfoResponseDto create(MultipartFile file, ClubCreateRequestDto dto);

    // 클럽 수정
    ClubInfoResponseDto update(Long clubId, ClubUpdateRequestDto dto);

    // 클럽 수정시 사진 변경
    DBFileDto changeCoverPic(MultipartFile file, Long clubId);

    DBFileDto getCoverPicUri(Long clubId);

    // 조회 수 증가
    HttpStatus plusViewCount(Long clubId);

    // 권한 변경
    HttpStatus changeAuthority(Long clubId, Long memberId, String authority);

    HttpStatus delete(Long clubId);

    // 클럽 전체 조회
    Page<ClubSimpleInfoResponseDto> getAllClub(Pageable pageable);

    // 클럽 상세 조회
    ClubInfoResponseDto getOneClub(Long clubId);

    // 현재 클럽 간편 정보 리턴
    ClubSimpleInfoResponseDto getOneSimpleClub(Long clubId);

    String getMemberAuthority(Long clubId);

    // 클럽 구성원 리스트
    List<ClubMemberResponseDto> getMembersInClub(Long clubId);

    ClubInfoForUpdateResponseDto getInfoForUpdateClub(Long clubId);

    void addMember(Club club, Member member);

    HttpStatus removeMe(Long clubId);

    HttpStatus removeMember(Long clubId, Long memberId);

    boolean checkCanApply(Long clubId);

    ClubFormInfoResponseDto applyClub(Long clubId, ClubApplicationRequestDto dto);

    // 모든 클럽 신청서 조회
    List<ClubFormSimpleInfoResponseDto> getAllClubForm(Long clubId);

//    List<ClubFormInfoResponseDto> getAllFormByClubNickname(Long clubId, String nickname);

    ClubFormInfoResponseDto getOneClubForm(Long clubId, Long memberId);

    HttpStatus approval(Long clubId, Long memberId);

    HttpStatus reject(Long clubId, Long memberId);
}
