package com.mh.match.group.club.service;

import com.mh.match.common.entity.GroupAuthority;
import com.mh.match.common.entity.PublicScope;
import com.mh.match.common.entity.RecruitmentState;
import com.mh.match.common.exception.CustomException;
import com.mh.match.common.exception.ErrorCode;
import com.mh.match.group.club.dto.response.*;
import com.mh.match.group.club.entity.ClubApplicationForm;
import com.mh.match.group.club.repository.ClubApplicationFormRepository;
import com.mh.match.group.club.repository.ClubRepository;
import com.mh.match.group.club.repository.ClubTopicRepository;
import com.mh.match.group.club.repository.MemberClubRepository;
import com.mh.match.group.clubboard.board.entity.ClubBoard;
import com.mh.match.group.clubboard.board.repository.ClubBoardRepository;
import com.mh.match.group.clubboard.board.service.ClubBoardService;
import com.mh.match.group.project.entity.Project;
import com.mh.match.group.project.repository.ProjectRepository;
import com.mh.match.group.study.entity.Study;
import com.mh.match.group.study.repository.StudyRepository;
import com.mh.match.member.dto.MemberSimpleInfoResponseDto;
import com.mh.match.member.entity.Member;
import com.mh.match.member.repository.MemberRepository;
import com.mh.match.s3.dto.DBFileDto;
import com.mh.match.s3.entity.DBFile;
import com.mh.match.s3.repository.DBFileRepository;
import com.mh.match.s3.service.S3Service;
import com.mh.match.group.club.dto.request.ClubApplicationRequestDto;
import com.mh.match.group.club.dto.request.ClubCreateRequestDto;
import com.mh.match.group.club.dto.request.ClubUpdateRequestDto;
import com.mh.match.group.club.entity.Club;
import com.mh.match.group.club.entity.ClubTopic;
import com.mh.match.group.club.entity.CompositeMemberClub;
import com.mh.match.group.club.entity.MemberClub;
import com.mh.match.util.SecurityUtil;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.apache.kafka.common.utils.SecurityUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

@Service
@Transactional(readOnly = true, rollbackFor = Exception.class)
// https://velog.io/@kdhyo/JavaTransactional-Annotation-%EC%95%8C%EA%B3%A0-%EC%93%B0%EC%9E%90-26her30h
@RequiredArgsConstructor
public class ClubServiceImpl implements ClubService {

    private final MemberRepository memberRepository;
    private final ClubRepository clubRepository;
    private final MemberClubRepository memberClubRepository;
    private final ClubApplicationFormRepository clubApplicationFormRepository;
    private final DBFileRepository dbFileRepository;
    private final ClubBoardRepository clubBoardRepository;
    private final ClubTopicRepository clubTopicRepository;
    private final StudyRepository studyRepository;
    private final ProjectRepository projectRepository;
    private final ClubBoardService clubBoardService;
    private final S3Service s3Service;

    // 클럽 생성
    @Transactional
    public ClubInfoResponseDto create(MultipartFile file, ClubCreateRequestDto dto) {
        Member member = findMember(SecurityUtil.getCurrentMemberId());
        Club club = Club.of(dto, member);
        clubRepository.save(club);
        if (file != null) {
            DBFile dbFile = s3Service.uploadFile(file, "club/" + Long.toString(club.getId()) + "/cover/");
            club.setCoverPic(dbFile);
        }
        makeBasicBoards(club);
        addTopics(club, dto.getTopics());

        CompositeMemberClub compositeMemberClub = new CompositeMemberClub(member, club);
        MemberClub memberClub = memberClubRepository
                .findById(compositeMemberClub)
                .orElseGet(() -> MemberClub.builder()
                        .compositeMemberClub(compositeMemberClub)
                        .isActive(true)
                        .registerDate(LocalDateTime.now())
                        .authority(GroupAuthority.소유자)
                        .build());
        club.addMember();
        memberClubRepository.save(memberClub);
        List<MemberSimpleInfoResponseDto> mem = new ArrayList<>();
        mem.add(MemberSimpleInfoResponseDto.from(member));
        return ClubInfoResponseDto.of(club, getClubTopics(club), mem, "소유자");
//        return ClubInfoResponseDto.of(club, getClubTopics(club),
//                findMemberInClub(club).stream().map(MemberSimpleInfoResponseDto::from).collect(
//                        Collectors.toList()), "소유자");
    }

    // 클럽 업데이트를 위한 정보
    public ClubInfoForUpdateResponseDto getInfoForUpdateClub(Long clubId) {
        Club club = findClub(clubId);
//        Member member = findMember(SecurityUtil.getCurrentMemberId());
        if (!SecurityUtil.getCurrentMemberId().equals(club.getMember().getId())) {
            throw new CustomException(ErrorCode.UNAUTHORIZED_CHANGE);
        }
        return ClubInfoForUpdateResponseDto.of(club, getClubTopics(club));
    }

    // 클럽의 주제 리스트
    private List<ClubTopicResponseDto> getClubTopics(Club club) {
        return clubTopicRepository.findAllByClub(club)
                .stream().map(ClubTopicResponseDto::from).collect(Collectors.toList());
    }

    // 클럽 업데이트
    @Transactional
    public ClubInfoResponseDto update(Long clubId, ClubUpdateRequestDto dto) {
        Club club = findClub(clubId);
        Member member = findMember(SecurityUtil.getCurrentMemberId());

        // 권한 체크
        checkAuthority(member, club);

        club.update(dto);
        addTopics(club, dto.getTopics());

        String authority = getMemberAuthority(clubId);

        return ClubInfoResponseDto.of(club, getClubTopics(club),
                findMemberInClub(club).stream().map(MemberSimpleInfoResponseDto::from).collect(
                        Collectors.toList()), authority);
    }

    // 사진 바꾸기
    @Transactional
    public DBFileDto changeCoverPic(MultipartFile file, Long clubId) {
        if (file == null) {
            return DBFileDto.of(null);
        }
        Club club = findClub(clubId);
        if (club.getCoverPic() != null) {
            s3Service.deleteS3File("club/" + Long.toString(clubId) + "/cover/" + club.getCoverPic().getId());
        }
        DBFile dbFile = s3Service.uploadFile(file, "club/" + Long.toString(clubId) + "/cover/");
        club.setCoverPic(dbFile);
        return DBFileDto.of(dbFile);
    }

    // 사진 정보만 가져오기
    public DBFileDto getCoverPicUri(Long clubId) {
        return DBFileDto.of(findClub(clubId).getCoverPic());
    }

    // 클럽 조회수 증가
    @Transactional
    public HttpStatus plusViewCount(Long clubId) {
        findClub(clubId).plusViewCount();
        return HttpStatus.OK;
    }

    // 권한 변경
    @Transactional
    public HttpStatus changeAuthority(Long clubId, Long memberId, String authority) {
        Club club = findClub(clubId);
        Member changer = findMember(SecurityUtil.getCurrentMemberId());
        Member member = findMember(memberId);

        MemberClub ms = memberClubRepository.findById(
                        new CompositeMemberClub(member, club))
                .orElseThrow(() -> new CustomException(ErrorCode.MEMBER_CLUB_NOT_FOUND));

        MemberClub msChanger = memberClubRepository.findById(
                        new CompositeMemberClub(changer, club))
                .orElseThrow(() -> new CustomException(ErrorCode.MEMBER_CLUB_NOT_FOUND));
        // 권한 변경 권한에 관한 로직
        // 소유자만이 권한을 변경할 수 있다
        if (!msChanger.getAuthority().equals(GroupAuthority.소유자)) {
            throw new CustomException(ErrorCode.UNAUTHORIZED_CHANGE);
        }
        // 소유자는 양도가 가능하다
        if (authority.equals("소유자")) {
            club.setMember(member);
            msChanger.setAuthority(GroupAuthority.관리자);
        }
        ms.setAuthority(GroupAuthority.from(authority));
        return HttpStatus.OK;
    }

    // 클럽를 업데이트, 삭제할 권한이 있는지 체크
    public void checkAuthority(Member member, Club club) {
        MemberClub ms = memberClubRepository.findById(
                        new CompositeMemberClub(member, club))
                .orElseThrow(() -> new CustomException(ErrorCode.MEMBER_CLUB_NOT_FOUND));
        if (!ms.getIsActive()) {
            throw new CustomException(ErrorCode.MEMBER_CLUB_NOT_FOUND);
        }

        if (!ms.getAuthority().equals(GroupAuthority.소유자)) {
            throw new CustomException(ErrorCode.UNAUTHORIZED_CHANGE);
        }
    }

    @Transactional
    public HttpStatus delete(Long clubId) {
        Club club = findClub(clubId);
        Member member = findMember(SecurityUtil.getCurrentMemberId());

        checkAuthority(member, club);

        // 클럽 게시판, 게시글, 댓글 삭제 정책 회의 후 생성
        List<ClubBoard> clubBoards = clubBoardRepository.findAllByClub(club);
        if (!clubBoards.isEmpty()) {
            for (ClubBoard clubBoard : clubBoards) {
                clubBoardService.deleteBoard(clubBoard.getId());
            }
            // 클럽 주제 제거
            clubTopicRepository.deleteAllByClub(club);
        }
        // 클럽 Cover 제거
        if (club.getCoverPic() != null) {
            s3Service.deleteS3File("club/" + Long.toString(clubId) + "/cover/"+ club.getCoverPic().getId());
            String uuid = club.getCoverPic().getId();
            club.initialCoverPic();
            dbFileRepository.deleteById(uuid);
        }

        // 속한 스터디, 프로젝트 초기화
        initialize(club);

        // 클럽 멤버 비활성화
        List<MemberClub> memberStudies = memberClubRepository.findMemberRelationInClub(club);
        if (!memberStudies.isEmpty()) {
            for (MemberClub mem : memberStudies) {
                mem.deActivation();
            }
        }
        club.deActivation();
        return HttpStatus.OK;
    }

    @Transactional
    public void initialize(Club club) {
        List<Study> studys = studyRepository.findAllByClub(club);
        List<Project> projects = projectRepository.findAllByClub(club);
        if (!studys.isEmpty()) {
            for (Study study : studys) {
                study.removeClub();
            }
        }
        if (!projects.isEmpty()) {
            for (Project project : projects) {
                project.removeClub();
            }
        }
    }

    // 클럽 전체 조회
    public Page<ClubSimpleInfoResponseDto> getAllClub(Pageable pageable) {
        return clubRepository.findAllClub(RecruitmentState.RECRUITMENT,
                        PublicScope.PUBLIC, pageable)
                .map(m -> ClubSimpleInfoResponseDto.of(m, getClubTopics(m)));
    }

    // 클럽 상세 조회
    public ClubInfoResponseDto getOneClub(Long clubId) {
        Club club = findClub(clubId);
        // 삭제된 클럽일 경우
        if (!club.getIsActive()) {
            throw new CustomException(ErrorCode.DELETED_CLUB);
        }
        String authority = getMemberAuthority(clubId);

        return ClubInfoResponseDto.of(club, getClubTopics(club),
                findMemberInClub(club).stream().map(MemberSimpleInfoResponseDto::from).collect(
                        Collectors.toList()), authority);
    }

    // 현재 클럽 간편 정보 리턴
    public ClubSimpleInfoResponseDto getOneSimpleClub(Long clubId) {
        Club club = findClub(clubId);
        return ClubSimpleInfoResponseDto.of(club, getClubTopics(club));
    }

    // 현 사용자의 권한 확인
    public String getMemberAuthority(Long clubId) {
        Club club = findClub(clubId);
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || authentication instanceof AnonymousAuthenticationToken) {
            return "방문객";
        } else {
            Member member = findMember(SecurityUtil.getCurrentMemberId());
            List<MemberClub> memberClubs = memberClubRepository.findMemberRelationInClub(club);

            String authority = "게스트";
            for (MemberClub memberClub : memberClubs) {
                if (memberClub.getCompositeMemberClub().getMember().getId().equals(member.getId())) {
                    authority = memberClub.getAuthority().toString();
                    break;
                }
            }
            return authority;
        }
    }

    // 클럽 구성원 리스트
    public List<ClubMemberResponseDto> getMembersInClub(Long clubId) {
        return memberClubRepository.findMemberRelationInClub(findClub(clubId))
                .stream()
                .map(ClubMemberResponseDto::from)
                .collect(Collectors.toList());
    }

    // 클럽 주제 추가, 변경
    @Transactional
    public void addTopics(Club club, List<String> topics) {
        clubTopicRepository.deleteAllByClub(club);
        if (topics.isEmpty()) {
            return;
        }
        for (String topic : topics) {
            clubTopicRepository.save(ClubTopic.of(club, topic));
        }
    }

    @Transactional
    public void addMember(Club club, Member member) {
        CompositeMemberClub compositeMemberClub = new CompositeMemberClub(member, club);

        // DB에 해당 멤버 기록이 없다면 새로 생성
        MemberClub memberClub = memberClubRepository
                .findById(compositeMemberClub)
                .orElseGet(() -> MemberClub.builder()
                        .compositeMemberClub(compositeMemberClub)
                        .registerDate(LocalDateTime.now())
                        .build());

        memberClub.setAuthority(GroupAuthority.팀원);
        memberClub.activation();
        club.addMember();
        memberClubRepository.save(memberClub);
    }

    // 클럽 탈퇴
    @Transactional
    public HttpStatus removeMe(Long clubId) {
        Club club = findClub(clubId);
        Member member = findMember(SecurityUtil.getCurrentMemberId());
        // 이미 탈퇴되었는지 여부
        CompositeMemberClub compositeMemberClub = new CompositeMemberClub(member, club);
        MemberClub memberClub = memberClubRepository.findById(compositeMemberClub)
                .orElseThrow(() -> new CustomException(ErrorCode.MEMBER_CLUB_NOT_FOUND));
        if (!memberClub.getIsActive()) {
            throw new CustomException(ErrorCode.MEMBER_CLUB_NOT_FOUND);
        }

        if (memberClub.getAuthority().equals(GroupAuthority.소유자)) {
            throw new CustomException(ErrorCode.HOST_CANNOT_LEAVE);
        }

        memberClub.deActivation();
        club.removeMember();
        return HttpStatus.OK;
    }

    // 클럽 추방
    @Transactional
    public HttpStatus removeMember(Long clubId, Long memberId) {
        Club club = findClub(clubId);
        Member remover = findMember(SecurityUtil.getCurrentMemberId());
        Member removed = findMember(memberId);

        MemberClub removerms = memberClubRepository.findById(
                        new CompositeMemberClub(remover, club))
                .orElseThrow(() -> new CustomException(ErrorCode.MEMBER_CLUB_NOT_FOUND));
        if (!removerms.getIsActive()) {
            throw new CustomException(ErrorCode.MEMBER_CLUB_NOT_FOUND);
        }

        MemberClub removedms = memberClubRepository.findById(
                        new CompositeMemberClub(removed, club))
                .orElseThrow(() -> new CustomException(ErrorCode.MEMBER_CLUB_NOT_FOUND));
        if (!removedms.getIsActive()) {
            throw new CustomException(ErrorCode.MEMBER_CLUB_NOT_FOUND);
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
        club.removeMember();
        return HttpStatus.OK;
    }

    @Transactional
    public void makeBasicBoards(Club club) {
        clubBoardRepository.save(new ClubBoard("공지사항", club));
        clubBoardRepository.save(new ClubBoard("일반게시판", club));
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

    // 현재 클럽에 속한 멤버 리스트
    public List<Member> findMemberInClub(Club club) {
        return memberClubRepository.findMemberInClub(club);
    }

    // 신청 버튼 클릭시 신청 가능한 인원인지 확인(조건 위배시 boolean? error?)
    public boolean checkCanApply(Long clubId) {
        Member member = findMember(SecurityUtil.getCurrentMemberId());
        Club club = findClub(clubId);

        // 신청 가능 확인 로직 (삭제, 모집, 이미 가입된 멤버인지 여부 확인)
        if (club.getIsActive().equals(Boolean.FALSE) || club.getRecruitmentState()
                .equals(RecruitmentState.FINISH) || !checkAlreadyJoin(club, member)) {
            return false;
        }
        // 신청 여부
        CompositeMemberClub cmc = new CompositeMemberClub(member, club);
        Optional<ClubApplicationForm> form = clubApplicationFormRepository.findById(cmc);
        if (form.isPresent()) {
//            throw new CustomException(ErrorCode.ALREADY_APPLY);
            return false;
        }

        return true;
    }

    // 클럽 가입 여부 확인 로직
    public boolean checkAlreadyJoin(Club club, Member member) {
        Optional<MemberClub> ms = memberClubRepository.findById(
                new CompositeMemberClub(member, club));
        if (ms.isPresent() && ms.get().getIsActive()) {
//            throw new CustomException(ErrorCode.ALREADY_JOIN);
            return false;
        }
        return true;
    }

    @Transactional
    public ClubFormInfoResponseDto applyClub(Long clubId, ClubApplicationRequestDto dto) {
        Member member = findMember(SecurityUtil.getCurrentMemberId());
        Club club = findClub(clubId);

        CompositeMemberClub cmc = new CompositeMemberClub(member, club);
        ClubApplicationForm clubApplicationForm = ClubApplicationForm.of(dto, cmc,
                member.getName());

        return ClubFormInfoResponseDto.from(
                clubApplicationFormRepository.save(clubApplicationForm));
    }

    // 모든 신청서 작성일 기준 내림차순 조회
    public List<ClubFormSimpleInfoResponseDto> getAllClubForm(Long clubId) {
        Club club = findClub(clubId);
        Member member = findMember(SecurityUtil.getCurrentMemberId());

        // 조회 권한 확인 로직
        MemberClub mc = memberClubRepository.findById(
                        new CompositeMemberClub(member, club))
                .orElseThrow(() -> new CustomException(ErrorCode.MEMBER_CLUB_NOT_FOUND));
        if (!mc.getIsActive()) {
            throw new CustomException(ErrorCode.MEMBER_CLUB_NOT_FOUND);
        }

        // 팀원은 신청서를 조회할 권한이 없음
        if (mc.getAuthority().equals(GroupAuthority.팀원)) {
            throw new CustomException(ErrorCode.UNAUTHORIZED_SELECT);
        }

        return clubApplicationFormRepository.findAllByCompositeMemberClub_ClubOrderByCreateDateDesc(club)
                .stream()
                .map(ClubFormSimpleInfoResponseDto::from)
                .collect(Collectors.toList());
    }

    // 신청서 목록의 복합 기본키를 가져와 해당 신청서 상세조회
    public ClubFormInfoResponseDto getOneClubForm(Long clubId, Long memberId) {
        CompositeMemberClub cmc = new CompositeMemberClub(findMember(memberId),
                findClub(clubId));

        ClubApplicationForm form = clubApplicationFormRepository.oneFormById(cmc)
                .orElseThrow(() -> new CustomException(ErrorCode.APPLIY_FORM_NOT_FOUND));

        return ClubFormInfoResponseDto.from(form);
    }

    // 가입 승인
    @Transactional
    public HttpStatus approval(Long clubId, Long memberId) {
        Club club = findClub(clubId);
        Member member = findMember(memberId);
        Member approver = findMember(SecurityUtil.getCurrentMemberId());

        if (!checkAlreadyJoin(club, member)) {
            throw new CustomException(ErrorCode.ALREADY_JOIN);
        }

        checkAuthorityApprovalReject(approver, club);

        addMember(club, member);
        reject(clubId, memberId);

        return HttpStatus.OK;
    }

    // 신청서 제거
    @Transactional
    public HttpStatus reject(Long clubId, Long memberId) {
        Member rejector = findMember(SecurityUtil.getCurrentMemberId());
        Club club = findClub(clubId);
        checkAuthorityApprovalReject(rejector, club);

        clubApplicationFormRepository.delete(
                validClubApplicationForm(findMember(memberId), club));

        return HttpStatus.OK;
    }

    public ClubApplicationForm validClubApplicationForm(Member member, Club club) {
        return clubApplicationFormRepository
                .findById(new CompositeMemberClub(member, club))
                .orElseThrow(() -> new CustomException(ErrorCode.APPLIY_FORM_NOT_FOUND));
    }

    public void checkAuthorityApprovalReject(Member member, Club club) {
        // 승인자의 권한 체크
        MemberClub ms = memberClubRepository.findById(
                        new CompositeMemberClub(member, club))
                .orElseThrow(() -> new CustomException(ErrorCode.MEMBER_CLUB_NOT_FOUND));
        if (!ms.getIsActive()) {
            throw new CustomException(ErrorCode.MEMBER_CLUB_NOT_FOUND);
        }
        // 팀원은 신청서를 조회, 수락, 거절할 권한이 없음
        if (ms.getAuthority().equals(GroupAuthority.팀원)) {
            throw new CustomException(ErrorCode.UNAUTHORIZED_SELECT);
        }
    }

}