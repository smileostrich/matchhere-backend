package com.mh.match.member.service;

import com.mh.match.common.entity.DetailPosition;
import com.mh.match.common.entity.Level;
import com.mh.match.common.entity.Techstack;
import com.mh.match.common.repository.TechstackRepository;
import com.mh.match.member.dto.ChangePasswordDto;
import com.mh.match.member.dto.request.*;
import com.mh.match.member.dto.response.*;
import com.mh.match.member.entity.*;
import com.mh.match.member.repository.*;
import com.mh.match.s3.dto.DBFileDto;
import com.mh.match.s3.entity.DBFile;
import com.mh.match.s3.repository.DBFileRepository;
import com.mh.match.s3.service.S3Service;
import com.mh.match.common.dto.DetailPositionInterface;
import com.mh.match.group.club.dto.response.ClubSimpleInfoResponseDto;
import com.mh.match.group.club.dto.response.ClubTopicResponseDto;
import com.mh.match.group.club.entity.Club;
import com.mh.match.group.club.repository.ClubTopicRepository;
import com.mh.match.group.club.repository.MemberClubRepository;
import com.mh.match.group.project.dto.response.ProjectSimpleInfoResponseDto;
import com.mh.match.group.project.dto.response.ProjectTechstackResponseDto;
import com.mh.match.group.project.entity.Project;
import com.mh.match.group.project.repository.ProjectTechstackRepository;
import com.mh.match.group.study.dto.response.StudySimpleInfoResponseDto;
import com.mh.match.group.study.dto.response.StudyTopicResponseDto;
import com.mh.match.group.study.entity.Study;
import com.mh.match.group.study.repository.MemberStudyRepository;
import com.mh.match.group.study.repository.StudyTopicRepository;
import com.mh.match.member.dto.inter.CareerInterface;
import com.mh.match.member.dto.inter.CertificationInterface;
import com.mh.match.member.dto.inter.EducationInterface;
import com.mh.match.member.dto.inter.MemberTechstackInterface;
import com.mh.match.member.entity.composite.CompositeMemberTechstack;
import com.mh.match.group.project.repository.MemberProjectRepository;
import com.mh.match.common.repository.DetailPositionRepository;
import com.mh.match.util.SecurityUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
@RequiredArgsConstructor
public class MemberService {
    private final AuthenticationManagerBuilder authenticationManagerBuilder;
    private final MemberRepository memberRepository;
    private final DBFileRepository dbFileRepository;
    private final MemberClubRepository memberClubRepository;
    private final MemberProjectRepository memberProjectRepository;
    private final MemberSnsRepository memberSnsRepository;
    private final DetailPositionRepository detailPositionRepository;
    private final TechstackRepository techstackRepository;
    private final PasswordEncoder passwordEncoder;
    private final MemberStudyRepository memberStudyRepository;
    private final MemberTechstackRepository memberTechstackRepository;
    private final CareerRepository careerRepository;
    private final CertificationRepository certificationRepository;
    private final EducationRepository educationRepository;
    private final ProjectTechstackRepository projectTechstackRepository;
    private final StudyTopicRepository studyTopicRepository;
    private final ClubTopicRepository clubTopicRepository;
    private final S3Service s3Service;


    @Transactional(readOnly = true)
    public MemberMeResponseDto getMe() {
        Member member = memberRepository.findById(SecurityUtil.getCurrentMemberId()).orElseThrow(() -> new NullPointerException("잘못된 토큰입니다."));
        MemberMeResponseDto memberMeResponseDto = MemberMeResponseDto.of(member);
        return memberMeResponseDto;
    }

    @Transactional(readOnly = true)
    public Boolean checkPassword(MemberCheckPasswordDto memberCheckPasswordDto) {
        UsernamePasswordAuthenticationToken authenticationToken = memberCheckPasswordDto.toAuthentication();
        Authentication authentication = authenticationManagerBuilder.getObject().authenticate(authenticationToken);
        if (authentication.isAuthenticated()) {
            return Boolean.TRUE;
        }
        return Boolean.FALSE;
    }

    @Transactional
    public HttpStatus updatePassword(ChangePasswordDto changePasswordDto) {
        Member member = memberRepository.findById(SecurityUtil.getCurrentMemberId()).orElseThrow(() -> new NullPointerException("잘못된 토큰입니다."));
        changePassword(member, changePasswordDto);
        return HttpStatus.OK;
    }

    @Transactional(readOnly = true)
    public MypageResponseDto getMyPage(String email) {
        Member member = memberRepository.findByEmail(email)
                .orElseThrow(() -> new NullPointerException("유저가 없습니다."));
        List<CareerInterface> careers = careerRepository.findAllByMember(member);
        List<EducationInterface> educations = educationRepository.findAllByMember(member);
        List<CertificationInterface> certifications = certificationRepository.findAllByMember(member);
        List<MemberTechstackInterface> techList = memberTechstackRepository.findTechstackByMember(member);
        List<MemberSns> snsList = memberSnsRepository.findAllByMember(member);
        List<DetailPositionInterface> dpositionList = detailPositionRepository.findAllByMemberWithInterface(member);
        MypageResponseDto mypageResponseDto = MypageResponseDto.of(member, careers, educations, certifications, techList, snsList, dpositionList);
        return mypageResponseDto;
    }

    @Transactional(readOnly = true)
    public MypageResponseDto getMyPage() {
        Member member = memberRepository.findById(SecurityUtil.getCurrentMemberId())
                .orElseThrow(() -> new NullPointerException("유저가 없습니다."));
        List<CareerInterface> careers = careerRepository.findAllByMember(member);
        List<EducationInterface> educations = educationRepository.findAllByMember(member);
        List<CertificationInterface> certifications = certificationRepository.findAllByMember(member);
        List<MemberTechstackInterface> techList = memberTechstackRepository.findTechstackByMember(member);
        List<MemberSns> snsList = memberSnsRepository.findAllByMember(member);
        List<DetailPositionInterface> dpositionList = detailPositionRepository.findAllByMemberWithInterface(member);
        MypageResponseDto mypageResponseDto = MypageResponseDto.of(member, careers, educations, certifications, techList, snsList, dpositionList);
        return mypageResponseDto;
    }

    @Transactional(readOnly = true)
    public MemberBasicinfoResponseDto getMemberBasicinfo() {
        Member member = memberRepository.findById(SecurityUtil.getCurrentMemberId())
                .orElseThrow(() -> new NullPointerException("유저가 없습니다."));
        return MemberBasicinfoResponseDto.of(member);
    }

    @Transactional
    public HttpStatus updateMemberBasicInfo(MemberBasicInfoRequestDto memberBasicinfoRequestDto) throws Exception {
        Member member = memberRepository.findById(SecurityUtil.getCurrentMemberId()).orElseThrow(() -> new NullPointerException("토큰이 잘못되었거나 존재하지 않는 사용자입니다."));
        validNickname(member, memberBasicinfoRequestDto.getNickname());
        memberBasicinfoRequestDto.setMember(member);
        return HttpStatus.OK;
    }

    @Transactional(readOnly = true)
    public MemberCareerAllResponseDto getMemberCareerAll() {
        Member member = memberRepository.findById(SecurityUtil.getCurrentMemberId())
                .orElseThrow(() -> new NullPointerException("유저가 없습니다."));
        List<CareerInterface> careers = careerRepository.findAllByMember(member);
        List<EducationInterface> educations = educationRepository.findAllByMember(member);
        List<CertificationInterface> certifications = certificationRepository.findAllByMember(member);

        MemberCareerAllResponseDto memberCareerAllResponseDto = MemberCareerAllResponseDto.of(careers, educations, certifications);
        return memberCareerAllResponseDto;
    }

    @Transactional(readOnly = true)
    public CareerResponseDto getMemberCareer(Long id) {
        CareerResponseDto careerResponseDto = careerRepository.findById(id).map(CareerResponseDto::of).orElseThrow(() -> new RuntimeException("해당 경력이 없습니다!"));
        return careerResponseDto;
    }

    @Transactional
    public CareerResponseDto createMemberCareer(MemberCareerRequestDto memberCareerRequestDto) {
        Member member = memberRepository.findById(SecurityUtil.getCurrentMemberId()).orElseThrow(() -> new NullPointerException("토큰이 잘못되었거나 존재하지 않는 사용자입니다."));
        Career career = memberCareerRequestDto.toCareer(member);
        careerRepository.save(career);
        return CareerResponseDto.of(career);
    }

    @Transactional
    public CareerResponseDto updateMemberCareer(Long id, MemberCareerUpdateRequestDto memberCareerUpdateRequestDto) {
        Member member = memberRepository.findById(SecurityUtil.getCurrentMemberId()).orElseThrow(() -> new NullPointerException("토큰이 잘못되었거나 존재하지 않는 사용자입니다."));
        Career career = careerRepository.findByMemberAndId(member,id).orElseThrow(() -> new NullPointerException("잘못된 사용자이거나 혹은 존재하지 않는 경력입니다!"));
        memberCareerUpdateRequestDto.setCareer(career);
        return CareerResponseDto.of(career);
    }

    @Transactional
    public HttpStatus deleteMemberCareer(Long id) {
        Member member = memberRepository.findById(SecurityUtil.getCurrentMemberId()).orElseThrow(() -> new NullPointerException("토큰이 잘못되었거나 존재하지 않는 사용자입니다."));
        Career career = careerRepository.findByMemberAndId(member, id).orElseThrow(() -> new NullPointerException("잘못된 사용자이거나 혹은 존재하지 않는 경력입니다!"));
        careerRepository.delete(career);
        return HttpStatus.OK;
    }

    @Transactional(readOnly = true)
    public CertificationResponseDto getMemberCertification(Long id) {
        CertificationResponseDto certificationResponseDto = certificationRepository.findById(id).map(CertificationResponseDto::of).orElseThrow(() -> new RuntimeException("해당 자격증이 없습니다!"));
        return certificationResponseDto;
    }

    @Transactional
    public CertificationResponseDto createMemberCertification(MemberCertificationRequestDto memberCertificationRequestDto) {
        Member member = memberRepository.findById(SecurityUtil.getCurrentMemberId()).orElseThrow(() -> new NullPointerException("토큰이 잘못되었거나 존재하지 않는 사용자입니다."));
        Certification certification = memberCertificationRequestDto.toCertification(member);
        certificationRepository.save(certification);
        return CertificationResponseDto.of(certification);
    }

    @Transactional
    public CertificationResponseDto updateMemberCertification(Long id, MemberCertificationUpdateRequestDto memberCertificationUpdateRequestDto) {
        Member member = memberRepository.findById(SecurityUtil.getCurrentMemberId()).orElseThrow(() -> new NullPointerException("토큰이 잘못되었거나 존재하지 않는 사용자입니다."));
        Certification certification = certificationRepository.findByMemberAndId(member, id).orElseThrow(() -> new NullPointerException("잘못된 사용자이거나 혹은 존재하지 않는 자격증입니다!"));
        memberCertificationUpdateRequestDto.setCertification(certification);
        return CertificationResponseDto.of(certification);
    }

    @Transactional
    public HttpStatus deleteMemberCertification(Long id) {
        Member member = memberRepository.findById(SecurityUtil.getCurrentMemberId()).orElseThrow(() -> new NullPointerException("토큰이 잘못되었거나 존재하지 않는 사용자입니다."));
        Certification certification = certificationRepository.findByMemberAndId(member, id).orElseThrow(() -> new NullPointerException("잘못된 사용자이거나 혹은 존재하지 않는 자격증입니다!"));
        certificationRepository.delete(certification);
        return HttpStatus.OK;
    }

    @Transactional(readOnly = true)
    public EducationResponseDto getMemberEducation(Long id) {
        EducationResponseDto educationResponseDto = educationRepository.findById(id).map(EducationResponseDto::of).orElseThrow(() -> new RuntimeException("해당 교육이 없습니다!"));
        return educationResponseDto;
    }

    @Transactional
    public EducationResponseDto createMemberEducation(MemberEducationRequestDto memberEducationRequestDto) {
        Member member = memberRepository.findById(SecurityUtil.getCurrentMemberId()).orElseThrow(() -> new NullPointerException("토큰이 잘못되었거나 존재하지 않는 사용자입니다."));
        Education education = memberEducationRequestDto.toCareer(member);
        educationRepository.save(education);
        return EducationResponseDto.of(education);
    }

    @Transactional
    public HttpStatus deleteMemberEducation(Long id) {
        Member member = memberRepository.findById(SecurityUtil.getCurrentMemberId()).orElseThrow(() -> new NullPointerException("토큰이 잘못되었거나 존재하지 않는 사용자입니다."));
        Education education = educationRepository.findByMemberAndId(member, id).orElseThrow(() -> new NullPointerException("잘못된 사용자이거나 혹은 존재하지 않는 교육입니다!"));
        educationRepository.delete(education);
        return HttpStatus.OK;
    }

    @Transactional
    public EducationResponseDto updateMemberEducation(Long id, MemberEducationUpdateRequestDto memberEducationUpdateRequestDto) {
        Member member = memberRepository.findById(SecurityUtil.getCurrentMemberId()).orElseThrow(() -> new NullPointerException("토큰이 잘못되었거나 존재하지 않는 사용자입니다."));
        Education education = educationRepository.findByMemberAndId(member, id).orElseThrow(() -> new NullPointerException("잘못된 사용자이거나 혹은 존재하지 않는 교육입니다!"));
        memberEducationUpdateRequestDto.setEducation(education);
        return EducationResponseDto.of(education);
    }

    @Transactional(readOnly = true)
    public MemberSkillResponseDto getMemberSkills() {
        Member member = memberRepository.findById(SecurityUtil.getCurrentMemberId())
                .orElseThrow(() -> new NullPointerException("유저가 없습니다."));
        List<DetailPositionInterface> dpositionList = detailPositionRepository.findAllByMemberWithInterface(member);
        List<MemberTechstackInterface> techList = memberTechstackRepository.findTechstackByMember(member);

        MemberSkillResponseDto memberSkillResponseDto = MemberSkillResponseDto.of(member, dpositionList, techList);
        return memberSkillResponseDto;
    }

    @Transactional
    public HttpStatus updateMemberSkills(MemberSkillRequestDto memberSkillRequestDto) throws Exception {
        Member member = memberRepository.findById(SecurityUtil.getCurrentMemberId()).orElseThrow(() -> new NullPointerException("토큰이 잘못되었거나 존재하지 않는 사용자입니다."));
        updateTechList(member, memberSkillRequestDto.getTechList());
        updateDposition(member, memberSkillRequestDto.getDpositionList());
        updatePosition(member, memberSkillRequestDto.getPosition());
        return HttpStatus.OK;
    }

    @Transactional(readOnly = true)
    public MemberSnsPortfolioResponseDto getMemberSnsPortfolio() {
        Member member = memberRepository.findById(SecurityUtil.getCurrentMemberId())
                .orElseThrow(() -> new NullPointerException("유저가 없습니다."));
        List<MemberSns> snsList = memberSnsRepository.findAllByMember(member);
        MemberSnsPortfolioResponseDto memberSnsPortfolioResponseDto = MemberSnsPortfolioResponseDto.of(member, snsList);
        return memberSnsPortfolioResponseDto;
    }

//    @Transactional
//    public HttpStatus createMemberPortfolio(MemberPortfolioRequestDto memberPortfolioRequestDto) throws Exception {
//        Member member = memberRepository.findById(SecurityUtil.getCurrentMemberId()).orElseThrow(() -> new NullPointerException("토큰이 잘못되었거나 존재하지 않는 사용자입니다."));
//        setPortfolioUuid(member, memberPortfolioRequestDto.getPortfolio_uuid());
//        if (!memberPortfolioRequestDto.getPortfolio_uri().isEmpty()) {
//            member.setPortfolio_uri(memberPortfolioRequestDto.getPortfolio_uri());
//        }
//        return HttpStatus.OK;
//    }

    @Transactional(readOnly = true)
    public PortfolioResponseDto getMemberPortfolio() {
        Member member = memberRepository.findById(SecurityUtil.getCurrentMemberId())
                .orElseThrow(() -> new NullPointerException("유저가 없습니다."));
        PortfolioResponseDto portfolioResponseDto = PortfolioResponseDto.of(member);
        return portfolioResponseDto;
    }

    @Transactional
    public PortfolioResponseDto updateMemberPortfolio(MultipartFile file, MemberPortfolioRequestDto memberPortfolioRequestDto) throws Exception {
        Member member = memberRepository.findById(SecurityUtil.getCurrentMemberId()).orElseThrow(() -> new NullPointerException("토큰이 잘못되었거나 존재하지 않는 사용자입니다."));

        if (file != null) {
            if (member.getPortfolio() != null) {
                s3Service.deleteS3File("member/" + Long.toString(member.getId()) + "/portfolio/"+ member.getPortfolio().getId());
                dbFileRepository.delete(member.getPortfolio());
            }
            DBFile dbFile = s3Service.uploadFile(file, "member/" + Long.toString(member.getId()) + "/portfolio/");
            member.setPortfolio(dbFile);
        }
        member.setPortfolio_uri(memberPortfolioRequestDto.getPortfolio_uri());
        return PortfolioResponseDto.of(member);
    }

    @Transactional
    public MemberSnsResponseDto updateMemberSns(MemberSnsRequestDto memberSnsRequestDto) {
        Member member = memberRepository.findById(SecurityUtil.getCurrentMemberId()).orElseThrow(() -> new NullPointerException("토큰이 잘못되었거나 존재하지 않는 사용자입니다."));
        List<MemberSns> memberSns = updateSns(member, memberSnsRequestDto.getSnsList());
        return MemberSnsResponseDto.of(member, memberSns);
    }

    @Transactional
    public void deleteMember() {
        Member member = memberRepository.getById(SecurityUtil.getCurrentMemberId());
        deleteMem(member);
    }

    @Transactional
    public void deleteMem(Member member) {
//        파일 지우기
        DBFile dbFile = member.getPortfolio();
        if (dbFile != null) {
            s3Service.deleteS3File("member/" + Long.toString(member.getId()) + "/portfolio/"+ dbFile.getId());
            member.setPortfolio(null);
            dbFileRepository.delete(dbFile);
        }
        DBFile coverPic = member.getCover_pic();
        if (coverPic != null) {
            s3Service.deleteS3File("member/" + Long.toString(member.getId()) + "/cover/"+ coverPic.getId());
            member.setCover_pic(null);
            dbFileRepository.delete(coverPic);
        }

        careerRepository.deleteAllByMember(member);
        memberSnsRepository.deleteAllByMember(member);
        detailPositionRepository.deleteAllByMember(member);
        memberTechstackRepository.deleteAllByCompositeMemberTechstack_Member(member);
        certificationRepository.deleteAllByMember(member);
        educationRepository.deleteAllByMember(member);
        member.setIs_active(Boolean.FALSE);
    }

    @Transactional
    public void updatePosition(Member member, String position) {
        member.setPosition(position);
    }

//    @Transactional
//    public void updateSns(Member member, HashMap<String, String> snsList) {
//        if (!snsList.isEmpty()) {
//            snsList.forEach((strKey, strValue) -> {
//                Optional<MemberSns> memberSns = memberSnsRepository.findByMemberAndSnsName(member, strKey);
//                if (memberSns.isEmpty()) {
//                    MemberSns innerMemberSns = MemberSns.builder()
//                            .member(member)
//                            .snsAccount(strValue)
//                            .snsName(strKey)
//                            .build();
//                    memberSnsRepository.save(innerMemberSns);
//                } else {
//                    MemberSns innerMemberSns = memberSns.get();
//                    innerMemberSns.setSnsAccount(strValue);
//                    innerMemberSns.setSnsName(strKey);
//                }
//            });
//        }
//    }

    @Transactional(readOnly = true)
    public MemberSnsResponseDto getMemberSns() {
        Member member = memberRepository.findById(SecurityUtil.getCurrentMemberId())
                .orElseThrow(() -> new NullPointerException("유저가 없습니다."));
        List<MemberSns> snsList = memberSnsRepository.findAllByMember(member);
        MemberSnsResponseDto memberSnsResponseDto = MemberSnsResponseDto.of(member,snsList);
        return memberSnsResponseDto;
    }

    @Transactional
    public List<MemberSns> updateSns(Member member, HashMap<String, String> snsList) {
//        List<MemberSns> memberSns = memberSnsRepository.findAllByMember(member);
        List<MemberSns> output = new ArrayList<>();;
//        if (!memberSns.isEmpty()) {
//            memberSnsRepository.deleteAll(memberSns);
//        }
        memberSnsRepository.deleteAllByMember(member);
        if (!snsList.isEmpty()) {
            for (Map.Entry<String, String> entry : snsList.entrySet()) {
                MemberSns inner_memberSns = MemberSns
                        .builder()
                        .member(member)
                        .snsName(entry.getKey())
                        .snsAccount(entry.getValue())
                        .build();
                memberSnsRepository.save(inner_memberSns);
                output.add(inner_memberSns);
            }
        }
        return output;
    }

    @Transactional
    public void updateDposition(Member member, List<String> dpositionList) {
//        List<DetailPosition> detailPositions = detailPositionRepository.findAllByMember(member);
//        if (!detailPositions.isEmpty()) {
//            detailPositionRepository.deleteAll(detailPositions);
//        }
        detailPositionRepository.deleteAllByMember(member);
        if (dpositionList != null && !dpositionList.isEmpty()) {
            for (String dposition : dpositionList) {
                DetailPosition innerDposition = DetailPosition
                        .builder()
                        .member(member)
                        .name(dposition)
                        .build();
                detailPositionRepository.save(innerDposition);
            }
        }
    }

    @Transactional
    public void updateTechList(Member member, HashMap<String, String> techList) throws Exception {
//        List<MemberTechstack> memberTechstacks = memberTechstackRepository.findAllByCompositeMemberTechstack_Member(member);
//        if (!memberTechstacks.isEmpty()) {
//            memberTechstackRepository.deleteAll(memberTechstacks);
//        }
        memberTechstackRepository.deleteAllByCompositeMemberTechstack_Member(member);
//        if (techList != null && !techList.isEmpty()) {
        if (!techList.isEmpty()) {
            for (Map.Entry<String, String> entry : techList.entrySet()) {
                Techstack techstack = techstackRepository.findByName(entry.getKey())
                        .orElseThrow(() -> new NullPointerException("기술 스택 정보가 없습니다."));
                validLevel(entry.getValue());
                CompositeMemberTechstack compositeMemberTechstack = CompositeMemberTechstack
                        .builder()
                        .member(member)
                        .techstack(techstack)
                        .build();
                MemberTechstack memberTechstack = MemberTechstack.builder().compositeMemberTechstack(compositeMemberTechstack).level(entry.getValue()).build();
                memberTechstackRepository.save(memberTechstack);
            }
        }
    }

    @Transactional(readOnly = true)
    public void validLevel(String level) throws Exception {
        if (!Stream.of(Level.values()).map(Enum::name)
                .collect(Collectors.toList()).contains(level)) {
            throw new Exception("존재하지 않는 level입니다");
        }
    }

    @Transactional(readOnly = true)
    public void validNickname(Member member, String nickname) throws Exception {
        if (member.getNickname().equals(nickname)) {
            return;
        } else if (memberRepository.existsByNickname(nickname)) {
            throw new Exception("닉네임이 존재합니다!");
        }
    }

    @Transactional
    public void changePassword(Member member, ChangePasswordDto changePasswordDto) {
        member.setPassword(passwordEncoder.encode(changePasswordDto.getPassword()));
    }

    @Transactional
    public DBFileDto updateCoverPic(MultipartFile file) {
        if (file != null) {
            Member member = memberRepository.findById(SecurityUtil.getCurrentMemberId()).orElseThrow(() -> new NullPointerException("잘못된 토큰입니다."));
            if (member.getCover_pic() != null) {
                s3Service.deleteS3File("member/" + Long.toString(member.getId()) + "/cover/" + member.getCover_pic().getId());
                dbFileRepository.delete(member.getCover_pic());
            }
            DBFile dbFile = s3Service.uploadFile(file, "member/" + Long.toString(member.getId()) + "/cover/");
            member.setCover_pic(dbFile);
            return DBFileDto.of(dbFile);
        }
        return DBFileDto.of(null);

    }

    @Transactional(readOnly = true)
    public MemberGroupResponseDto getMemberGroup() {
        Member member = memberRepository.findById(SecurityUtil.getCurrentMemberId()).orElseThrow(() -> new NullPointerException("잘못된 토큰입니다."));
        List<Project> projects = memberProjectRepository.projectInMember(member);
        List<Study> studies = memberStudyRepository.studyInMember(member);
        List<Club> clubs = memberClubRepository.findClubByMember(member);
        List<ProjectSimpleInfoResponseDto> projectDtos = new ArrayList<>();
        List<StudySimpleInfoResponseDto> studyDtos = new ArrayList<>();
        List<ClubSimpleInfoResponseDto> clubDtos = new ArrayList<>();
        for (Project project : projects) {
           projectDtos.add(ProjectSimpleInfoResponseDto.of(project, projectTechstackSimple(project)));
        }
        for (Study study : studies) {
            studyDtos.add(StudySimpleInfoResponseDto.of(study, getStudyTopics(study)));
        }
        for (Club club : clubs) {
            clubDtos.add(ClubSimpleInfoResponseDto.of(club, getClubTopics(club)));
        }
//        List<ProjectSimpleInfoResponseDto> projectDtos = (List<ProjectSimpleInfoResponseDto>) projects.stream().map(m -> ));
//        List<StudySimpleInfoResponseDto> studyDtos = (List<StudySimpleInfoResponseDto>) studies.stream().map(m -> StudySimpleInfoResponseDto.of(m, getStudyTopics(m)));
//        List<ClubSimpleInfoResponseDto> clubDtos = (List<ClubSimpleInfoResponseDto>) clubs.stream().map(m -> ClubSimpleInfoResponseDto.of(m, getClubTopics(m)));
        return MemberGroupResponseDto.of(projectDtos, studyDtos, clubDtos);
    }

    private List<ProjectTechstackResponseDto> projectTechstackSimple(Project project) {
        return projectTechstackRepository.findProjectTechstackByProject(project)
                .stream()
                .map(ProjectTechstackResponseDto::simple)
                .collect(Collectors.toList());
    }

    private List<StudyTopicResponseDto> getStudyTopics(Study study) {
        return studyTopicRepository.findAllByStudy(study)
                .stream().map(StudyTopicResponseDto::from).collect(Collectors.toList());
    }

    private List<ClubTopicResponseDto> getClubTopics(Club club) {
        return clubTopicRepository.findAllByClub(club)
                .stream().map(ClubTopicResponseDto::from).collect(Collectors.toList());
    }

    @Transactional
    public HttpStatus deleteMemberCoverPic() {
        Member member = memberRepository.findById(SecurityUtil.getCurrentMemberId()).orElseThrow(() -> new NullPointerException("잘못된 토큰입니다."));
        DBFile dbFile = member.getCover_pic();
        if (dbFile != null) {
            s3Service.deleteS3File("member/" + Long.toString(member.getId()) + "/cover/"+ dbFile.getId());
            member.setCover_pic(null);
            dbFileRepository.delete(dbFile);
        }
        return HttpStatus.OK;
    }

    @Transactional
    public HttpStatus deleteMemberPortfolio() {
        Member member = memberRepository.findById(SecurityUtil.getCurrentMemberId()).orElseThrow(() -> new NullPointerException("잘못된 토큰입니다."));
        DBFile dbFile = member.getPortfolio();
        if (dbFile != null) {
            s3Service.deleteS3File("member/" + Long.toString(member.getId()) + "/portfolio/"+ dbFile.getId());
            member.setPortfolio(null);
            dbFileRepository.delete(dbFile);
        }
        return HttpStatus.OK;
    }
}
