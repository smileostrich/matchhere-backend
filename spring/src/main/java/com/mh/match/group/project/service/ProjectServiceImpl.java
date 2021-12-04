package com.mh.match.group.project.service;

import com.mh.match.common.exception.CustomException;
import com.mh.match.common.exception.ErrorCode;
import com.mh.match.common.repository.TechstackRepository;
import com.mh.match.group.project.entity.CompositeProjectTechstack;
import com.mh.match.group.project.entity.ProjectTechstack;
import com.mh.match.group.project.repository.MemberProjectRepository;
import com.mh.match.group.project.repository.ProjectApplicationFormRepository;
import com.mh.match.group.project.repository.ProjectRepository;
import com.mh.match.group.project.repository.ProjectTechstackRepository;
import com.mh.match.group.projectboard.board.entity.ProjectBoard;
import com.mh.match.group.projectboard.board.repository.ProjectBoardRepository;
import com.mh.match.group.projectboard.board.service.ProjectBoardService;
import com.mh.match.member.dto.MemberSimpleInfoResponseDto;
import com.mh.match.member.entity.Member;
import com.mh.match.member.repository.MemberRepository;
import com.mh.match.s3.dto.DBFileDto;
import com.mh.match.s3.entity.DBFile;
import com.mh.match.s3.repository.DBFileRepository;
import com.mh.match.s3.service.S3Service;
import com.mh.match.common.entity.GroupAuthority;
import com.mh.match.common.entity.GroupCity;
import com.mh.match.common.entity.Level;
import com.mh.match.common.entity.ProjectProgressState;
import com.mh.match.common.entity.PublicScope;
import com.mh.match.common.entity.RecruitmentState;
import com.mh.match.common.entity.Techstack;
import com.mh.match.group.club.dto.response.ClubInfoForSelectResponseDto;
import com.mh.match.group.club.entity.Club;
import com.mh.match.group.club.repository.ClubRepository;
import com.mh.match.group.club.repository.MemberClubRepository;
import com.mh.match.group.project.dto.request.ProjectApplicationRequestDto;
import com.mh.match.group.project.dto.request.ProjectCreateRequestDto;
import com.mh.match.group.project.dto.request.ProjectUpdateRequestDto;
import com.mh.match.group.project.dto.response.ProjectFormInfoResponseDto;
import com.mh.match.group.project.dto.response.ProjectFormSimpleInfoResponseDto;
import com.mh.match.group.project.dto.response.ProjectInfoForCreateResponseDto;
import com.mh.match.group.project.dto.response.ProjectInfoForUpdateResponseDto;
import com.mh.match.group.project.dto.response.ProjectInfoResponseDto;
import com.mh.match.group.project.dto.response.ProjectMemberResponseDto;
import com.mh.match.group.project.dto.response.ProjectSimpleInfoResponseDto;
import com.mh.match.group.project.dto.response.ProjectTechstackResponseDto;
import com.mh.match.group.project.entity.CompositeMemberProject;
import com.mh.match.group.project.entity.MemberProject;
import com.mh.match.group.project.entity.Project;
import com.mh.match.group.project.entity.ProjectApplicationForm;
import com.mh.match.util.SecurityUtil;
import java.time.LocalDateTime;
import java.util.HashMap;
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
public class ProjectServiceImpl implements ProjectService {

    private final MemberRepository memberRepository;
    private final ProjectRepository projectRepository;
    private final ClubRepository clubRepository;
    private final MemberClubRepository memberClubRepository;
    private final MemberProjectRepository memberProjectRepository;
    private final ProjectApplicationFormRepository projectApplicationFormRepository;
    private final TechstackRepository techstackRepository;
    private final ProjectTechstackRepository projectTechstackRepository;
    private final DBFileRepository dbFileRepository;
    private final ProjectBoardRepository projectBoardRepository;
    private final ProjectBoardService projectBoardService;
    private final S3Service s3Service;

    // 프로젝트 생성을 위한 정보(회원의 클럽 리스트)
    public ProjectInfoForCreateResponseDto getInfoForCreate() {
        return ProjectInfoForCreateResponseDto.from(makeClubInfoForSelectResponseDtos(
                memberClubRepository.findClubByMember(findMember(SecurityUtil.getCurrentMemberId()))));
    }

    // 프로젝트 생성
    @Transactional
    public ProjectInfoResponseDto create(MultipartFile file, ProjectCreateRequestDto dto) {
//    public ProjectInfoResponseDto create(MultipartFile file, ProjectCreateRequestDto dto) {
        validCity(dto.getCity());
        Member member = findMember(SecurityUtil.getCurrentMemberId());

        Project project = new Project(dto, findClub(dto.getClubId()), member);

        projectRepository.save(project);

        if (file != null) {
            DBFile dbFile = s3Service.uploadFile(file, "project/" + Long.toString(project.getId()) + "/cover/");
            project.setCoverPic(dbFile);
        }

        makeBasicBoards(project); // 기본 공지사항, 게시판 생성
        addTechstack(project, dto.getTechstacks());

        // 프로젝트에 소유자 정보 등록
        CompositeMemberProject compositeMemberProject = new CompositeMemberProject(member, project);
        MemberProject memberProject = MemberProject.builder()
                .compositeMemberProject(compositeMemberProject)
                .role(dto.getHostPosition())
                .registerDate(LocalDateTime.now())
                .authority(GroupAuthority.소유자)
                .isActive(true)
                .build();
        project.addRole(dto.getHostPosition());
        memberProjectRepository.save(memberProject);

        return ProjectInfoResponseDto.of(project, projectTechstackFull(project),
                memberRole(project, "개발자"), memberRole(project, "기획자"), memberRole(project, "디자이너"),
                "소유자");
    }

    // 프로젝트 업데이트를 위한 정보
    public ProjectInfoForUpdateResponseDto getInfoForUpdateProject(Long projectId) {
        Project project = findProject(projectId);

        if (!SecurityUtil.getCurrentMemberId().equals(project.getMember().getId())) {
            throw new CustomException(ErrorCode.UNAUTHORIZED_CHANGE);
        }

        return ProjectInfoForUpdateResponseDto.of(project,
                projectTechstackSimple(project), makeClubInfoForSelectResponseDtos(
                        memberClubRepository.findClubByMember(
                                findMember(SecurityUtil.getCurrentMemberId()))));
    }

    public MemberProject findMemberProject(Member member, Project project) {
        return memberProjectRepository.findMemberProjectByCompositeMemberProject(
                        new CompositeMemberProject(member, project))
                .orElseThrow(() -> new CustomException(ErrorCode.MEMBER_PROJECT_NOT_FOUND));
    }

    // 프로젝트 업데이트
    @Transactional
    public ProjectInfoResponseDto update(Long projectId, ProjectUpdateRequestDto dto) {
        validCity(dto.getCity());
        Project project = findProject(projectId);
        Member member = findMember(SecurityUtil.getCurrentMemberId());
        // 권한 체크
        getProjectAuthority(member, project);

        project.update(dto, findClub(dto.getClubId()));
        addTechstack(project, dto.getTechstacks());

        return getOneProject(projectId);
    }
    // 사진 바꾸기
    @Transactional
    public DBFileDto changeCoverPic(MultipartFile file, Long projectId){
        if (file == null) {
            return DBFileDto.of(null);
        }
        Project project = findProject(projectId);
        if (project.getCoverPic() != null) {
            s3Service.deleteS3File("project/" + Long.toString(projectId) + "/cover/" + project.getCoverPic().getId());
        }
        DBFile dbFile = s3Service.uploadFile(file, "project/" + Long.toString(projectId) + "/cover/");
        project.setCoverPic(dbFile);
        return DBFileDto.of(dbFile);
    }
    // 사진 정보만 가져오기
    public DBFileDto getCoverPicUri(Long projectId) {
        return DBFileDto.of(findProject(projectId).getCoverPic());
    }

    public void getProjectAuthority(Member member, Project project){
        MemberProject mp = memberProjectRepository.findById(
                        new CompositeMemberProject(member, project))
                .orElseThrow(() -> new CustomException(ErrorCode.MEMBER_PROJECT_NOT_FOUND));

        if (!mp.getAuthority().equals(GroupAuthority.소유자)) {
            throw new CustomException(ErrorCode.UNAUTHORIZED_CHANGE);
        }
    }

    // 프로젝트 삭제
    @Transactional
    public HttpStatus delete(Long projectId) {
        Project project = findProject(projectId);
        Member member = findMember(SecurityUtil.getCurrentMemberId());
        // 권한 체크
//        Optional<MemberProject> mp = memberProjectRepository.findMemberProjectByCompositeMemberProjectMemberAndCompositeMemberProject_Project(member, project);
        MemberProject mp = memberProjectRepository.findById(new CompositeMemberProject(member, project))
                .orElseThrow(() -> new CustomException(ErrorCode.MEMBER_PROJECT_NOT_FOUND));
        if (!mp.getAuthority().equals(GroupAuthority.소유자)) {
            throw new CustomException(ErrorCode.UNAUTHORIZED_CHANGE);
        }
        List<ProjectBoard> projectBoards = projectBoardRepository.findAllByProject(project);
        for (ProjectBoard projectBoard : projectBoards) {
            projectBoardService.deleteBoard(projectBoard.getId());
        }
        // 프로젝트 기술 스택 제거 (안지워도 될수도?)
        projectTechstackRepository.deleteAllByProject(project);

        // 프로젝트 Cover 제거
        if (project.getCoverPic() != null) {
            s3Service.deleteS3File("project/" + Long.toString(projectId) + "/cover/"+project.getCoverPic().getId());
            dbFileRepository.delete(project.getCoverPic());
        }
        project.setCoverPic(null);

        // 프로젝트 멤버 비활성화
        List<MemberProject> memberProjects = memberProjectRepository.findMemberRelationInProject(
                project);
        for (MemberProject mem : memberProjects) {
            mem.deactivation();
        }
        // 프로젝트 비활성화
        project.setIsActive(Boolean.FALSE);
        return HttpStatus.OK;
    }

    // 모든 프로젝트 간단 조회  + (공개 범위 고려)
    public Page<ProjectSimpleInfoResponseDto> getAllProject(Pageable pageable) {
//        Page<Project> projects = projectRepository.findAllProject(ProjectProgressState.FINISH,
//                RecruitmentState.RECRUITMENT, PublicScope.PUBLIC, pageable);
        Page<Project> projects = projectRepository.findAllProject(PublicScope.PUBLIC, pageable);
        return projects.map(m -> ProjectSimpleInfoResponseDto.of(m, projectTechstackSimple(m)));
    }

    // 추천 프로젝트 조회
//    public List<ProjectSimpleInfoResponseDto> getRecommendationProject(Pageable pageable) {
////        Pageable limit = PageRequest.of(0, 10);
//        List<Project> projects = projectRepository.findTop10OrderByCreateDateDesc(pageable);
//
//        List<ProjectSimpleInfoResponseDto> projectInfoResponseDtos = new ArrayList<>();
//        for (Project project : projects) {
//            projectInfoResponseDtos.add(
//                ProjectSimpleInfoResponseDto.of(project, projectTechstackSimple(project)));
//        }
//        return projectInfoResponseDtos;
//    }

    // 현재 프로젝트 정보 리턴
    public ProjectInfoResponseDto getOneProject(Long projectId) {
        Project project = findProject(projectId);

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || authentication instanceof AnonymousAuthenticationToken) {
            String authority = "방문객";
            return ProjectInfoResponseDto.of(project, projectTechstackFull(project),
                    memberRole(project, "개발자"), memberRole(project, "기획자"), memberRole(project, "디자이너"),
                    authority);
        } else {
            Member member = findMember(SecurityUtil.getCurrentMemberId());
            List<MemberProject> mps = memberProjectRepository.findMemberRelationInProject(project);

            String authority = "게스트";
            for (MemberProject mp : mps) {
                if (mp.getCompositeMemberProject().getMember().getId().equals(member.getId())) {
                    authority = mp.getAuthority().toString();
                    break;
                }
            }
            return ProjectInfoResponseDto.of(project, projectTechstackFull(project),
                    memberRole(project, "개발자"), memberRole(project, "기획자"), memberRole(project, "디자이너"),
                    authority);
        }
    }
    // 현 사용자의 권한 확인
    public String getMemberAuthority(Long projectId){
        Project project = findProject(projectId);
        Member member = findMember(SecurityUtil.getCurrentMemberId());
        List<MemberProject> mps = memberProjectRepository.findMemberRelationInProject(project);

        String authority = "게스트";
        for (MemberProject mp : mps) {
            if (mp.getCompositeMemberProject().getMember().getId().equals(member.getId())) {
                authority = mp.getAuthority().toString();
                break;
            }
        }
        return authority;
    }


    // 현재 프로젝트 간편 정보 리턴
    public ProjectSimpleInfoResponseDto getOneSimpleProject(Long projectId) {
        Project project = findProject(projectId);
        return ProjectSimpleInfoResponseDto.of(project, projectTechstackSimple(project));
    }

    // 프로젝트 기술 스택 간단한 정보
    public List<ProjectTechstackResponseDto> projectTechstackSimple(Project project) {
        return projectTechstackRepository.findProjectTechstackByProject(project)
                .stream()
                .map(ProjectTechstackResponseDto::simple)
                .collect(Collectors.toList());
    }

    // 프로젝트 기술 스택 전체 정보
    public List<ProjectTechstackResponseDto> projectTechstackFull(Project project) {
        return projectTechstackRepository.findProjectTechstackByProject(project)
                .stream()
                .map(ProjectTechstackResponseDto::full)
                .collect(Collectors.toList());
    }

    // 현재 프로젝트 구성원 리스트 리턴
    public List<ProjectMemberResponseDto> memberInProject(Long projectId) {
        return memberProjectRepository.findMemberRelationInProject(findProject(projectId))
                .stream()
                .map(ProjectMemberResponseDto::from)
                .collect(Collectors.toList());
    }

    // 특정 프로젝트의 특정 역할인 멤버 간편 정보 리스트
    public List<MemberSimpleInfoResponseDto> memberRole(Project project, String role) {
        return memberProjectRepository.findMemberRole(project, role)
                .stream()
                .map(MemberSimpleInfoResponseDto::from)
                .collect(Collectors.toList());
    }

    // 프로젝트 조회수 증가
    @Transactional
    public HttpStatus plusViewCount(Long projectId){
        findProject(projectId).plusViewCount();
        return HttpStatus.OK;
    }

    // 기술 스택 추가
    @Transactional
    public void addTechstack(Project project, HashMap<String, String> techstacks) {
        projectTechstackRepository.deleteAllByProject(project);
        if (techstacks == null) {
            return;
        }

        for (String tech : techstacks.keySet()) {
            Techstack techstack = findTechstack(tech);
            Level level = Level.from(techstacks.get(tech));

            CompositeProjectTechstack compositeProjectTechstack = new CompositeProjectTechstack(
                    techstack, project);

            projectTechstackRepository.save(new ProjectTechstack(compositeProjectTechstack, level));
        }
    }

    // 멤버 추가
    @Transactional
    public void addMember(Project project, Member member, String role) {
        CompositeMemberProject compositeMemberProject = new CompositeMemberProject(member, project);
        // DB에 해당 멤버 기록이 없다면 새로 생성
        MemberProject memberProject = memberProjectRepository
                .findById(compositeMemberProject)
                .orElseGet(() -> MemberProject.builder()
                        .compositeMemberProject(compositeMemberProject)
                        .build());

        project.addRole(role);
        memberProject.setRole(role);
        memberProject.setRegisterDate(LocalDateTime.now());
        memberProject.setAuthority(GroupAuthority.팀원);
        memberProject.activation();
        memberProjectRepository.save(memberProject);
    }

    // 멤버 탈퇴
    @Transactional
    public HttpStatus removeMe(Long projectId) {
        Project project = findProject(projectId);
        Member member = findMember(SecurityUtil.getCurrentMemberId());

        MemberProject mp = memberProjectRepository.findById(
                        new CompositeMemberProject(member, project))
                .orElseThrow(() -> new CustomException(ErrorCode.MEMBER_PROJECT_NOT_FOUND));

        if (mp.getAuthority().equals(GroupAuthority.소유자)) {
            throw new CustomException(ErrorCode.HOST_CANNOT_LEAVE);
        }

        CompositeMemberProject compositeMemberProject = new CompositeMemberProject(member, project);
        // DB에 해당 멤버 기록이 없다면 예외 발생
        MemberProject memberProject = memberProjectRepository.findById(compositeMemberProject)
                .orElseThrow(() -> new CustomException(ErrorCode.MEMBER_NOT_FOUND));

        project.removeRole(memberProject.getRole());
        memberProject.deactivation();
        return HttpStatus.OK;
    }

    // 멤버 추방
    @Transactional
    public HttpStatus removeMember(Long projectId, Long memberId) {
        Project project = findProject(projectId);
        Member remover = findMember(SecurityUtil.getCurrentMemberId());
        Member removed = findMember(memberId);

        MemberProject removermp = memberProjectRepository.findById(
                        new CompositeMemberProject(remover, project))
                .orElseThrow(() -> new CustomException(ErrorCode.MEMBER_PROJECT_NOT_FOUND));

        MemberProject removedmp = memberProjectRepository.findById(
                        new CompositeMemberProject(removed, project))
                .orElseThrow(() -> new CustomException(ErrorCode.MEMBER_PROJECT_NOT_FOUND));

        if (removermp.getAuthority().equals(GroupAuthority.팀원)) { // 소유자와 관리자만이 추방 권한을 가짐
            throw new CustomException(ErrorCode.COMMON_MEMBER_CANNOT_REMOVE);
        }

        if (!removedmp.getAuthority().equals(GroupAuthority.팀원)) { // 소유자와 관리자는 추방될 수 없음
            throw new CustomException(ErrorCode.ONLY_CAN_REMOVE_COMMON);
        }

        project.removeRole(removedmp.getRole()); // 추방되는 사람의 역할 수 감소
        removedmp.deactivation(); // 비활성화
        return HttpStatus.OK;
    }

    // 역할 변경
    @Transactional
    public HttpStatus changeRole(Long projectId, Long memberId, String role) {
        Project project = findProject(projectId);
        Member member = findMember(memberId);

        MemberProject mp = memberProjectRepository.findById(
                        new CompositeMemberProject(member, project))
                .orElseThrow(() -> new CustomException(ErrorCode.MEMBER_PROJECT_NOT_FOUND));

        // 역할 변경 권한에 관한 로직
        // 소유자는 본인 이하의 권한을 가진 사람의 역할 변경 가능
        // 관리자는 본인 이하의 권한을 가진 사람의 역할 변경 가능
        // 팀원은 역할을 변경할 수 없다

        project.removeRole(mp.getRole());
        mp.setRole(role);
        project.addRole((role));
        return HttpStatus.OK;
    }

    // 권한 변경
    @Transactional
    public HttpStatus changeAuthority(Long projectId, Long memberId, String authority) {
        Project project = findProject(projectId);
        Member changer = findMember(SecurityUtil.getCurrentMemberId());
        Member member = findMember(memberId);

        MemberProject mp = memberProjectRepository.findById(
                        new CompositeMemberProject(member, project))
                .orElseThrow(() -> new CustomException(ErrorCode.MEMBER_PROJECT_NOT_FOUND));

        MemberProject mpChanger = memberProjectRepository.findById(
                        new CompositeMemberProject(changer, project))
                .orElseThrow(() -> new CustomException(ErrorCode.MEMBER_PROJECT_NOT_FOUND));
        // 권한 변경 권한에 관한 로직
        // 소유자만이 권한을 변경할 수 있다
        if (!mpChanger.getAuthority().equals(GroupAuthority.소유자)) {
            throw new CustomException(ErrorCode.UNAUTHORIZED_CHANGE);
        }
        // 소유자는 양도가 가능하다
        if (authority.equals("소유자")) {
            project.setMember(member);
            mpChanger.setAuthority(GroupAuthority.관리자);
        }
        mp.setAuthority(GroupAuthority.from(authority));
        return HttpStatus.OK;
    }

    // 프로젝트 찾기
    public Project findProject(Long projectId) {
        Project project = projectRepository.findById(projectId)
                .orElseThrow(() -> new CustomException(ErrorCode.PROJECT_NOT_FOUND));

        if (Boolean.FALSE.equals(project.getIsActive())) {
            throw new CustomException(ErrorCode.DELETED_PROJECT);
        }

        return project;
    }

    // 멤버 찾기
    public Member findMember(Long memberId) {
        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new CustomException(ErrorCode.MEMBER_NOT_FOUND));

        if (Boolean.FALSE.equals(member.getIs_active())) {
            throw new CustomException(ErrorCode.MEMBER_NOT_FOUND);
        }

        return member;
    }

    // 기술 스택 찾기
    public Techstack findTechstack(String techName) {
        return techstackRepository.findByName(techName)
                .orElseThrow(() -> new CustomException(ErrorCode.TECHSTACK_NOT_FOUND));
    }

    public Club findClub(Long clubId) {
        if (clubId == null) {
            return null;
        }
        return clubRepository.findById(clubId)
                .orElseThrow(() -> new CustomException(ErrorCode.CLUB_NOT_FOUND));
    }

    public void validCity(String city) {
        if (!Stream.of(GroupCity.values()).map(Enum::name)
                .collect(Collectors.toList()).contains(city)) {
            throw new CustomException(ErrorCode.CITY_NOT_FOUND);
        }
    }

    public List<ClubInfoForSelectResponseDto> makeClubInfoForSelectResponseDtos(List<Club> clubs) {
        return clubs.stream()
                .map(ClubInfoForSelectResponseDto::from)
                .collect(Collectors.toList());
    }

    public List<MemberSimpleInfoResponseDto> makeMemberSimpleInfoResponseDtos(
            List<Member> members) {
        return members.stream()
                .map(MemberSimpleInfoResponseDto::from)
                .collect(Collectors.toList());
    }

    // 기본 게시판 생성
    @Transactional
    public void makeBasicBoards(Project project) {
        projectBoardRepository.save(new ProjectBoard("공지사항", project));
        projectBoardRepository.save(new ProjectBoard("일반게시판", project));
    }

    // 신청 버튼 클릭시 관련 정보 및 권한 체크
    public HttpStatus checkCanApply(Long projectId) {
        Member member = findMember(SecurityUtil.getCurrentMemberId());
        Project project = findProject(projectId);
        // 신청 가능 확인 로직
        if (project.getProjectProgressState().equals(ProjectProgressState.FINISH)
                || project.getIsActive().equals(Boolean.FALSE) || project.getRecruitmentState()
                .equals(RecruitmentState.FINISH)) {
            throw new CustomException(ErrorCode.CANNOT_APPLY);
        }
        checkAlreadyJoin(project, member);

        return HttpStatus.OK;
    }

    // 프로젝트 가입 여부 확인 로직
    public void checkAlreadyJoin(Project project, Member member) {
        Optional<MemberProject> mp = memberProjectRepository.findMemberProjectByCompositeMemberProject(
                new CompositeMemberProject(member, project));
        if (mp.isPresent()) {
            throw new CustomException(ErrorCode.ALREADY_JOIN);
        }
    }

    @Transactional
    public ProjectFormInfoResponseDto applyProject(Long projectId, ProjectApplicationRequestDto dto) {
        Member member = findMember(SecurityUtil.getCurrentMemberId());
        Project project = findProject(projectId);
        checkAlreadyJoin(project, member);
        CompositeMemberProject cmp = new CompositeMemberProject(member, project);

        Optional<ProjectApplicationForm> form = projectApplicationFormRepository.findById(cmp);
        if (form.isPresent()) {
            throw new CustomException(ErrorCode.ALREADY_APPLY);
        }

        ProjectApplicationForm projectApplicationForm = ProjectApplicationForm.of(dto, cmp,
                member.getName());
        return ProjectFormInfoResponseDto.from(projectApplicationFormRepository.save(projectApplicationForm));
    }

    // 모든 신청서 작성일 기준 내림차순 조회
    public List<ProjectFormSimpleInfoResponseDto> allProjectForm(Long projectId) {
        Project project = findProject(projectId);
        Member member = findMember(SecurityUtil.getCurrentMemberId());
        // 조회 권한 확인 로직
        MemberProject mp = memberProjectRepository.findById(
                        new CompositeMemberProject(member, project))
                .orElseThrow(() -> new CustomException(ErrorCode.MEMBER_PROJECT_NOT_FOUND));
        if (mp.getAuthority().equals(GroupAuthority.팀원)) {
            throw new CustomException(ErrorCode.UNAUTHORIZED_SELECT);
        }
        return projectApplicationFormRepository.findAllByCompositeMemberProject_ProjectOrderByCreateDateDesc(project)
                .stream()
                .map(ProjectFormSimpleInfoResponseDto::from)
                .collect(Collectors.toList());

    }

    // 신청서 목록의 복합 기본키를 가져와 해당 신청서 상세조회 (프론트 방식에 따라 불필요할 수 있음)
    public ProjectFormInfoResponseDto oneProjectForm(Long projectId, Long memberId) {
        CompositeMemberProject cmp = new CompositeMemberProject(findMember(memberId),
                findProject(projectId));

        ProjectApplicationForm form = projectApplicationFormRepository.oneFormById(cmp)
                .orElseThrow(() -> new CustomException(ErrorCode.APPLIY_FORM_NOT_FOUND));

        return ProjectFormInfoResponseDto.from(form);
    }

    // 가입 승인
    @Transactional
    public HttpStatus approval(Long projectId, Long memberId) {
        Project project = findProject(projectId);
        Member member = findMember(memberId);

        checkAlreadyJoin(project, member);
        ProjectApplicationForm form = validProjectApplicationForm(member, project);

        addMember(project, member, form.getRole());
        reject(projectId, memberId);
        return HttpStatus.OK;
    }

    // 신청서 제거
    @Transactional
    public HttpStatus reject(Long projectId, Long memberId) {
        projectApplicationFormRepository.delete(
                validProjectApplicationForm(findMember(memberId), findProject(projectId)));
        return HttpStatus.OK;
    }

    public ProjectApplicationForm validProjectApplicationForm(Member member, Project project) {
        return projectApplicationFormRepository
                .findById(new CompositeMemberProject(member, project))
                .orElseThrow(() -> new CustomException(ErrorCode.APPLIY_FORM_NOT_FOUND));
    }
}