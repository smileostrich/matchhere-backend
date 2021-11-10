package com.mh.match.group.study.service;

import com.mh.match.common.exception.CustomException;
import com.mh.match.common.exception.ErrorCode;
import com.mh.match.group.project.dto.response.ProjectInfoResponseDto;
import com.mh.match.group.study.dto.response.*;
import com.mh.match.group.study.entity.StudyApplicationForm;
import com.mh.match.group.study.repository.MemberStudyRepository;
import com.mh.match.group.study.repository.StudyApplicationFormRepository;
import com.mh.match.group.study.repository.StudyRepository;
import com.mh.match.group.study.repository.StudyTopicRepository;
import com.mh.match.group.studyboard.board.entity.StudyBoard;
import com.mh.match.group.studyboard.board.repository.StudyBoardRepository;
import com.mh.match.group.studyboard.board.service.StudyBoardService;
import com.mh.match.member.dto.MemberSimpleInfoResponseDto;
import com.mh.match.member.entity.Member;
import com.mh.match.member.repository.MemberRepository;
import com.mh.match.s3.dto.DBFileDto;
import com.mh.match.s3.entity.DBFile;
import com.mh.match.s3.repository.DBFileRepository;
import com.mh.match.s3.service.S3Service;
import com.mh.match.common.entity.GroupAuthority;
import com.mh.match.common.entity.GroupCity;
import com.mh.match.common.entity.PublicScope;
import com.mh.match.common.entity.RecruitmentState;
import com.mh.match.common.entity.StudyProgressState;
import com.mh.match.group.club.dto.response.ClubInfoForSelectResponseDto;
import com.mh.match.group.club.entity.Club;
import com.mh.match.group.club.repository.ClubRepository;
import com.mh.match.group.club.repository.MemberClubRepository;
import com.mh.match.group.study.dto.request.StudyApplicationRequestDto;
import com.mh.match.group.study.dto.request.StudyCreateRequestDto;
import com.mh.match.group.study.dto.request.StudyUpdateRequestDto;
import com.mh.match.group.study.entity.CompositeMemberStudy;
import com.mh.match.group.study.entity.MemberStudy;
import com.mh.match.group.study.entity.Study;
import com.mh.match.group.study.entity.StudyTopic;
import com.mh.match.util.SecurityUtil;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

@Service
@Transactional(readOnly = true, rollbackFor = Exception.class)
@RequiredArgsConstructor
public class StudyServiceImpl implements StudyService {

    private final MemberRepository memberRepository;
    private final StudyRepository studyRepository;
    private final ClubRepository clubRepository;
    private final MemberStudyRepository memberStudyRepository;
    private final MemberClubRepository memberClubRepository;
    private final StudyApplicationFormRepository studyApplicationFormRepository;
    private final DBFileRepository dbFileRepository;
    private final StudyBoardRepository studyBoardRepository;
    private final StudyTopicRepository studyTopicRepository;
    private final StudyBoardService studyBoardService;
    private final S3Service s3Service;

    // 스터디 생성을 위한 정보(호스트의 클럽 정보)
    public StudyInfoForCreateResponseDto getInfoForCreate() {
        return StudyInfoForCreateResponseDto.from(makeClubInfoForSelectResponseDtos(
                findClubInMember(findMember(SecurityUtil.getCurrentMemberId()))));
    }

    // 스터디 생성
    @Transactional
    public StudyInfoResponseDto create(MultipartFile file, StudyCreateRequestDto dto) {
        validCity(dto.getCity());
        Member member = findMember(SecurityUtil.getCurrentMemberId());
        Study study = Study.of(dto, findClub(dto.getClubId()), member);
        studyRepository.save(study);

        if (file != null) {
            DBFile dbFile = s3Service.uploadFile(file, "study/" + Long.toString(study.getId()) + "/cover/");
            study.setCoverPic(dbFile);
        }

        makeBasicBoards(study);
        addTopics(study, dto.getTopics());

        CompositeMemberStudy compositeMemberStudy = new CompositeMemberStudy(member, study);
        MemberStudy memberStudy = memberStudyRepository
                .findById(compositeMemberStudy)
                .orElseGet(() -> MemberStudy.builder()
                        .compositeMemberStudy(compositeMemberStudy)
                        .isActive(true)
                        .registerDate(LocalDateTime.now())
                        .authority(GroupAuthority.소유자)
                        .build());

        study.addMember();
        memberStudyRepository.save(memberStudy);
        return StudyInfoResponseDto.of(study, getStudyTopics(study),
                findMemberInStudy(study).stream().map(MemberSimpleInfoResponseDto::from).collect(
                        Collectors.toList()), "소유자");
//        return getOneStudy(study.getId());
    }

    // 스터디 업데이트를 위한 정보
    public StudyInfoForUpdateResponseDto getInfoForUpdateStudy(Long studyId) {
        Study study = findStudy(studyId);
        Member member = findMember(SecurityUtil.getCurrentMemberId());
        if (!SecurityUtil.getCurrentMemberId().equals(study.getMember().getId())) {
            throw new CustomException(ErrorCode.UNAUTHORIZED_CHANGE);
        }

        return StudyInfoForUpdateResponseDto.of(study, getStudyTopics(study),
                makeClubInfoForSelectResponseDtos(findClubInMember(member)));
    }

    // 스터디의 주제 리스트
    private List<StudyTopicResponseDto> getStudyTopics(Study study) {
        return studyTopicRepository.findAllByStudy(study)
                .stream().map(StudyTopicResponseDto::from).collect(Collectors.toList());
    }

    // 스터디 업데이트
    @Transactional
    public StudyInfoResponseDto update(Long studyId, StudyUpdateRequestDto dto) {
        validCity(dto.getCity());
        Study study = findStudy(studyId);
        Member member = findMember(SecurityUtil.getCurrentMemberId());

        // 권한 체크
        checkAuthority(member, study);

        study.update(dto, findClub(dto.getClubId()));
        addTopics(study, dto.getTopics());

        return getOneStudy(studyId);
    }

    // 사진 바꾸기
    @Transactional
    public DBFileDto changeCoverPic(MultipartFile file, Long studyId) {
        if (file == null) {
            return DBFileDto.of(null);
        }
        Study study = findStudy(studyId);
        if (study.getCoverPic() != null) {
            s3Service.deleteS3File("study/" + Long.toString(studyId) + "/cover/" + study.getCoverPic().getId());
        }
        DBFile dbFile = s3Service.uploadFile(file, "study/" + Long.toString(studyId) + "/cover/");
        study.setCoverPic(dbFile);
        return DBFileDto.of(dbFile);
    }

    // 사진 정보만 가져오기
    public DBFileDto getCoverPicUri(Long studyId) {
        return DBFileDto.of(findStudy(studyId).getCoverPic());
    }

    // 스터디 조회수 증가
    @Transactional
    public HttpStatus plusViewCount(Long studyId) {
        findStudy(studyId).plusViewCount();
        return HttpStatus.OK;
    }

    // 권한 변경
    @Transactional
    public HttpStatus changeAuthority(Long studyId, Long memberId, String authority) {
        Study study = findStudy(studyId);
        Member changer = findMember(SecurityUtil.getCurrentMemberId());
        Member member = findMember(memberId);

        MemberStudy ms = memberStudyRepository.findById(
                        new CompositeMemberStudy(member, study))
                .orElseThrow(() -> new CustomException(ErrorCode.MEMBER_STUDY_NOT_FOUND));

        MemberStudy msChanger = memberStudyRepository.findById(
                        new CompositeMemberStudy(changer, study))
                .orElseThrow(() -> new CustomException(ErrorCode.MEMBER_STUDY_NOT_FOUND));
        // 권한 변경 권한에 관한 로직
        // 소유자만이 권한을 변경할 수 있다
        if (!msChanger.getAuthority().equals(GroupAuthority.소유자)) {
            throw new CustomException(ErrorCode.UNAUTHORIZED_CHANGE);
        }
        // 소유자는 양도가 가능하다
        if (authority.equals("소유자")) {
            study.setMember(member);
            msChanger.setAuthority(GroupAuthority.관리자);
        }
        ms.setAuthority(GroupAuthority.from(authority));
        return HttpStatus.OK;
    }

    // 스터디를 업데이트, 삭제할 권한이 있는지 체크
    public void checkAuthority(Member member, Study study) {
        MemberStudy ms = memberStudyRepository.findById(
                        new CompositeMemberStudy(member, study))
                .orElseThrow(() -> new CustomException(ErrorCode.MEMBER_STUDY_NOT_FOUND));
        if (!ms.getIsActive()) {
            throw new CustomException(ErrorCode.MEMBER_STUDY_NOT_FOUND);
        }

        if (!ms.getAuthority().equals(GroupAuthority.소유자)) {
            throw new CustomException(ErrorCode.UNAUTHORIZED_CHANGE);
        }
    }

    @Transactional
    public HttpStatus delete(Long studyId) {
        Study study = findStudy(studyId);
        Member member = findMember(SecurityUtil.getCurrentMemberId());

        checkAuthority(member, study);

        // 스터디 주제 제거
        studyTopicRepository.deleteAllByStudy(study);
        // 스터디 게시판, 게시글, 댓글 삭제 정책 회의 후 생성
        List<StudyBoard> studyBoards = studyBoardRepository.findAllByStudy(study);
        for (StudyBoard studyBoard : studyBoards) {
            studyBoardService.deleteBoard(studyBoard.getId());
        }
        // 스터디 Cover 제거
        if (study.getCoverPic() != null) {
            s3Service.deleteS3File("study/" + Long.toString(studyId) + "/cover/"+study.getCoverPic().getId());
            dbFileRepository.delete(study.getCoverPic());
        }
        study.setCoverPic(null);
        // 스터디 멤버 비활성화
        List<MemberStudy> memberStudies = memberStudyRepository.findMemberRelationInStudy(study);
        for (MemberStudy mem : memberStudies) {
            mem.deActivation();
        }
        study.deActivation();
        return HttpStatus.OK;
    }

    // 스터디 전체 조회
    public Page<StudySimpleInfoResponseDto> getAllStudy(Pageable pageable) {
        return studyRepository.findAllStudy(StudyProgressState.FINISH, RecruitmentState.RECRUITMENT,
                        PublicScope.PUBLIC, pageable)
                .map(m -> StudySimpleInfoResponseDto.of(m, getStudyTopics(m)));
    }

    // 스터디 상세 조회
    public StudyInfoResponseDto getOneStudy(Long studyId) {
        Study study = findStudy(studyId);

        // 삭제된 스터디일 경우
        if (!study.getIsActive()) {
            throw new CustomException(ErrorCode.DELETED_STUDY);
        }

//        String authority = "게스트";
        String authority = getMemberAuthority(studyId);

        // 비공개 스터디에는 소속된 멤버 + 초대링크를 가진 사람들만 조회하는 로직 필요

        return StudyInfoResponseDto.of(study, getStudyTopics(study),
                findMemberInStudy(study).stream().map(MemberSimpleInfoResponseDto::from).collect(
                        Collectors.toList()), authority);
    }

    // 현재 스터디 간편 정보 리턴
    public StudySimpleInfoResponseDto getOneSimpleStudy(Long studyId) {
        Study study = findStudy(studyId);
        return StudySimpleInfoResponseDto.of(study, getStudyTopics(study));
    }

    // 현 사용자의 권한 확인
    public String getMemberAuthority(Long studyId) {
        Study study = findStudy(studyId);
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || authentication instanceof AnonymousAuthenticationToken) {
            return "방문객";
        } else {
            Member member = findMember(SecurityUtil.getCurrentMemberId());
            List<MemberStudy> mss = memberStudyRepository.findMemberRelationInStudy(study);

            String authority = "게스트";
            for (MemberStudy ms : mss) {
                if (ms.getCompositeMemberStudy().getMember().getId().equals(member.getId())) {
                    authority = ms.getAuthority().toString();
                    break;
                }
            }
            return authority;
        }
    }

    // 스터디 구성원 리스트
    public List<StudyMemberResponseDto> getMembersInStudy(Long studyId) {
        return memberStudyRepository.findMemberRelationInStudy(findStudy(studyId))
                .stream()
                .map(StudyMemberResponseDto::from)
                .collect(Collectors.toList());
    }

    @Transactional
    public void addTopics(Study study, List<String> topics) {
        studyTopicRepository.deleteAllByStudy(study);
        if (topics.isEmpty()) {
            return;
        }
        for (String topic : topics) {
            studyTopicRepository.save(StudyTopic.of(study, topic));
        }
    }

    @Transactional
    public void addMember(Study study, Member member) {
        CompositeMemberStudy compositeMemberStudy = new CompositeMemberStudy(member, study);

        // DB에 해당 멤버 기록이 없다면 새로 생성
        MemberStudy memberStudy = memberStudyRepository
                .findById(compositeMemberStudy)
                .orElseGet(() -> MemberStudy.builder()
                        .compositeMemberStudy(compositeMemberStudy)
                        .registerDate(LocalDateTime.now())
                        .build());

        memberStudy.setAuthority(GroupAuthority.팀원);
        memberStudy.activation();
        study.addMember();
        memberStudyRepository.save(memberStudy);
    }

    // 스터디 탈퇴
    @Transactional
    public HttpStatus removeMe(Long studyId) {
        Study study = findStudy(studyId);
        Member member = findMember(SecurityUtil.getCurrentMemberId());
        // 이미 탈퇴되었는지 여부
        CompositeMemberStudy compositeMemberStudy = new CompositeMemberStudy(member, study);
        MemberStudy memberStudy = memberStudyRepository.findById(compositeMemberStudy)
                .orElseThrow(() -> new CustomException(ErrorCode.MEMBER_STUDY_NOT_FOUND));
        if (!memberStudy.getIsActive()) {
            throw new CustomException(ErrorCode.MEMBER_STUDY_NOT_FOUND);
        }

        if (memberStudy.getAuthority().equals(GroupAuthority.소유자)) {
            throw new CustomException(ErrorCode.HOST_CANNOT_LEAVE);
        }

        memberStudy.deActivation();
        study.removeMember();
        return HttpStatus.OK;
    }

    // 스터디 추방
    @Transactional
    public HttpStatus removeMember(Long studyId, Long memberId) {
        Study study = findStudy(studyId);
        Member remover = findMember(SecurityUtil.getCurrentMemberId());
        Member removed = findMember(memberId);

        MemberStudy removerms = memberStudyRepository.findById(
                        new CompositeMemberStudy(remover, study))
                .orElseThrow(() -> new CustomException(ErrorCode.MEMBER_STUDY_NOT_FOUND));
        if (!removerms.getIsActive()) {
            throw new CustomException(ErrorCode.MEMBER_STUDY_NOT_FOUND);
        }

        MemberStudy removedms = memberStudyRepository.findById(
                        new CompositeMemberStudy(removed, study))
                .orElseThrow(() -> new CustomException(ErrorCode.MEMBER_STUDY_NOT_FOUND));
        if (!removedms.getIsActive()) {
            throw new CustomException(ErrorCode.MEMBER_STUDY_NOT_FOUND);
        }

        // 소유자와 관리자만이 추방 권한을 가짐
        if (removerms.getAuthority().equals(GroupAuthority.팀원)) {
            throw new CustomException(ErrorCode.COMMON_MEMBER_CANNOT_REMOVE);
        }
        // 소유자와 관리자는 추방될 수 없음
        if (!removedms.getAuthority().equals(GroupAuthority.팀원)) {
            throw new CustomException(ErrorCode.ONLY_CAN_REMOVE_COMMON);
        }

        removedms.deActivation();
        study.removeMember();
        return HttpStatus.OK;
    }

    @Transactional
    public void makeBasicBoards(Study study) {
        studyBoardRepository.save(new StudyBoard("공지사항", study));
        studyBoardRepository.save(new StudyBoard("일반게시판", study));
    }

    public Study findStudy(Long studyId) {
        Study study = studyRepository.findById(studyId)
                .orElseThrow(() -> new CustomException(ErrorCode.STUDY_NOT_FOUND));

        if (Boolean.FALSE.equals(study.getIsActive())) {
            throw new CustomException(ErrorCode.DELETED_STUDY);
        }

        return study;
    }

    public Member findMember(Long memberId) {
        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new CustomException(ErrorCode.MEMBER_NOT_FOUND));

        if (Boolean.FALSE.equals(member.getIs_active())) {
            throw new CustomException(ErrorCode.MEMBER_NOT_FOUND);
        }

        return member;
    }

    public Club findClub(Long clubId) {
        if (clubId == null) {
            return null;
        }
        return clubRepository.findById(clubId)
                .orElseThrow(() -> new CustomException(ErrorCode.CLUB_NOT_FOUND));
    }

    // 멤버가 가진 클럽 리스트
    public List<Club> findClubInMember(Member member) {
        return memberClubRepository.findClubByMember(member);
    }

    // 현재 스터디에 속한 멤버 리스트
    public List<Member> findMemberInStudy(Study study) {
        return memberStudyRepository.findMemberInStudy(study);
    }

    // 특정 멤버의 활성화 스터디 리스트
    public List<Study> studyInMember(Member member) {
        return memberStudyRepository.studyInMember(member);
    }

    public void validCity(String city) {
        if (!Stream.of(GroupCity.values()).map(Enum::name)
                .collect(Collectors.toList()).contains(city)) {
            throw new CustomException(ErrorCode.CITY_NOT_FOUND);
        }
    }

    // 클럽 정보 요약
    public List<ClubInfoForSelectResponseDto> makeClubInfoForSelectResponseDtos(List<Club> clubs) {
        return clubs.stream()
                .map(ClubInfoForSelectResponseDto::from)
                .collect(Collectors.toList());
    }

    // 신청 버튼 클릭시 신청 가능한 인원인지 확인(조건 위배시 boolean? error?)
    public boolean checkCanApply(Long studyId) {
        Member member = findMember(SecurityUtil.getCurrentMemberId());
        Study study = findStudy(studyId);

        // 신청 가능 확인 로직 (스터디 종료, 삭제, 모집, 이미 가입된 멤버인지 여부 확인)
        if (study.getStudyProgressState().equals(StudyProgressState.FINISH)
                || study.getIsActive().equals(Boolean.FALSE) || study.getRecruitmentState()
                .equals(RecruitmentState.FINISH) || !checkAlreadyJoin(study, member)) {
            return false;
        }
        // 신청 여부
        CompositeMemberStudy cms = new CompositeMemberStudy(member, study);
        Optional<StudyApplicationForm> form = studyApplicationFormRepository.findById(cms);
        if (form.isPresent()) {
//            throw new CustomException(ErrorCode.ALREADY_APPLY);
            return false;
        }

        return true;
    }

    // 스터디 가입 여부 확인 로직
    public boolean checkAlreadyJoin(Study study, Member member) {
        Optional<MemberStudy> ms = memberStudyRepository.findById(
                new CompositeMemberStudy(member, study));
        if (ms.isPresent() && ms.get().getIsActive()) {
//            throw new CustomException(ErrorCode.ALREADY_JOIN);
            return false;
        }
        return true;
    }

    @Transactional
    public StudyFormInfoResponseDto applyStudy(Long studyId, StudyApplicationRequestDto dto) {
        Member member = findMember(SecurityUtil.getCurrentMemberId());
        Study study = findStudy(studyId);

        CompositeMemberStudy cms = new CompositeMemberStudy(member, study);
        StudyApplicationForm studyApplicationForm = StudyApplicationForm.of(dto, cms,
                member.getName());

        return StudyFormInfoResponseDto.from(
                studyApplicationFormRepository.save(studyApplicationForm));
    }

    // 모든 신청서 작성일 기준 내림차순 조회
    public List<StudyFormSimpleInfoResponseDto> getAllStudyForm(Long studyId) {
        Study study = findStudy(studyId);
        Member member = findMember(SecurityUtil.getCurrentMemberId());

        // 조회 권한 확인 로직
        MemberStudy ms = memberStudyRepository.findById(
                        new CompositeMemberStudy(member, study))
                .orElseThrow(() -> new CustomException(ErrorCode.MEMBER_STUDY_NOT_FOUND));
        if (!ms.getIsActive()) {
            throw new CustomException(ErrorCode.MEMBER_STUDY_NOT_FOUND);
        }

        // 팀원은 신청서를 조회할 권한이 없음
        if (ms.getAuthority().equals(GroupAuthority.팀원)) {
            throw new CustomException(ErrorCode.UNAUTHORIZED_SELECT);
        }

        return studyApplicationFormRepository.findAllByCompositeMemberStudy_StudyOrderByCreateDateDesc(study)
                .stream()
                .map(StudyFormSimpleInfoResponseDto::from)
                .collect(Collectors.toList());
    }

    // 신청서 목록의 복합 기본키를 가져와 해당 신청서 상세조회
    public StudyFormInfoResponseDto getOneStudyForm(Long studyId, Long memberId) {
        CompositeMemberStudy cms = new CompositeMemberStudy(findMember(memberId),
                findStudy(studyId));

        StudyApplicationForm form = studyApplicationFormRepository.oneFormById(cms)
                .orElseThrow(() -> new CustomException(ErrorCode.APPLIY_FORM_NOT_FOUND));

        return StudyFormInfoResponseDto.from(form);
    }

    // 가입 승인
    @Transactional
    public HttpStatus approval(Long studyId, Long memberId) {
        Study study = findStudy(studyId);
        Member member = findMember(memberId);
        Member approver = findMember(SecurityUtil.getCurrentMemberId());

        if (!checkAlreadyJoin(study, member)) {
            throw new CustomException(ErrorCode.ALREADY_JOIN);
        }

        checkAuthorityApprovalReject(approver, study);

        addMember(study, member);
        reject(studyId, memberId);

        return HttpStatus.OK;
    }

    // 신청서 제거
    @Transactional
    public HttpStatus reject(Long studyId, Long memberId) {
        Member rejector = findMember(SecurityUtil.getCurrentMemberId());
        Study study = findStudy(studyId);
        checkAuthorityApprovalReject(rejector, study);

        studyApplicationFormRepository.delete(
                validStudyApplicationForm(findMember(memberId), study));

        return HttpStatus.OK;
    }

    public StudyApplicationForm validStudyApplicationForm(Member member, Study study) {
        return studyApplicationFormRepository
                .findById(new CompositeMemberStudy(member, study))
                .orElseThrow(() -> new CustomException(ErrorCode.APPLIY_FORM_NOT_FOUND));
    }

    public void checkAuthorityApprovalReject(Member member, Study study) {
        // 승인자의 권한 체크
        MemberStudy ms = memberStudyRepository.findById(
                        new CompositeMemberStudy(member, study))
                .orElseThrow(() -> new CustomException(ErrorCode.MEMBER_STUDY_NOT_FOUND));
        if (!ms.getIsActive()) {
            throw new CustomException(ErrorCode.MEMBER_STUDY_NOT_FOUND);
        }
        // 팀원은 신청서를 조회, 수락, 거절할 권한이 없음
        if (ms.getAuthority().equals(GroupAuthority.팀원)) {
            throw new CustomException(ErrorCode.UNAUTHORIZED_SELECT);
        }
    }
}