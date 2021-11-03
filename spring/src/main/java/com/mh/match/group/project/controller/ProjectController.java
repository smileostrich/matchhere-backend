package com.mh.match.group.project.controller;

import com.mh.match.s3.dto.DBFileDto;
import com.mh.match.group.project.dto.request.ProjectCreateRequestDto;
import com.mh.match.group.project.dto.request.ProjectUpdateRequestDto;
import com.mh.match.group.project.dto.response.ProjectInfoForCreateResponseDto;
import com.mh.match.group.project.dto.response.ProjectInfoForUpdateResponseDto;
import com.mh.match.group.project.dto.response.ProjectInfoResponseDto;
import com.mh.match.group.project.dto.response.ProjectMemberResponseDto;
import com.mh.match.group.project.dto.response.ProjectSimpleInfoResponseDto;
import com.mh.match.group.project.service.ProjectService;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import java.util.List;
import javax.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.data.web.SortDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequiredArgsConstructor
@RequestMapping("/project")
public class ProjectController {

    private final ProjectService projectService;

    @GetMapping("/myclublist")
    @ApiOperation(value = "프로젝트 생성을 위한 정보", notes = "<strong>프로젝트를 생성하기 위한</strong> 생성할 멤버의 클럽을 받는다")
    @ApiResponses({
        @ApiResponse(code = 200, message = "정보 조회"),
        @ApiResponse(code = 404, message = "MEMBER_NOT_FOUND"),
    })
    public ResponseEntity<ProjectInfoForCreateResponseDto> getInfoForCreate() {
        return new ResponseEntity<>(projectService.getInfoForCreate(), HttpStatus.OK);
    }

    @GetMapping("/infoforupdate/{projectId}")
    @ApiOperation(value = "프로젝트 업데이트를 위한 정보",
        notes = "<strong>받은 프로젝트 id</strong>로 해당 프로젝트 정보 + 수정을 위한 정보(사용자 클럽 리스트, 지역, 상태 리스트 등")
    @ApiResponses({
        @ApiResponse(code = 200, message = "정보 조회"),
        @ApiResponse(code = 401, message = "UNAUTHORIZED_CHANGE"),
        @ApiResponse(code = 404, message = "MEMBER_NOT_FOUND\nPROJECT_NOT_FOUND"),
    })
    public ResponseEntity<ProjectInfoForUpdateResponseDto> getInfoForUpdate(
        @PathVariable("projectId") Long projectId) {
        return new ResponseEntity<>(projectService.getInfoForUpdateProject(projectId),
            HttpStatus.OK);
    }

    @PostMapping(value = "", consumes = { MediaType.APPLICATION_JSON_VALUE, MediaType.MULTIPART_FORM_DATA_VALUE })
    @ApiOperation(value = "프로젝트 생성", notes = "<strong>받은 프로젝트 정보</strong>를 사용해서 프로젝트을 생성한다.")
    @ApiResponses({
        @ApiResponse(code = 200, message = "프로젝트 정보"),
        @ApiResponse(code = 400, message = "DEVELOPER_COUNT_OVER\nPLANNER_COUNT_OVER\nDESIGNER_COUNT_OVER"
            + "\nDEVELOPER_COUNT_BELOW_ZERO\nPLANNER_COUNT_BELOW_ZERO\nDESIGNER_COUNT_BELOW_ZERO"),
        @ApiResponse(code = 404, message = "CITY_NOT_FOUND\nMEMBER_NOT_FOUND\nCLUB_NOT_FOUND\nFILE_NOT_FOUND"
            + "TECHSTACK_NOT_FOUND\nLEVEL_NOT_FOUND\nROLE_NOT_FOUND"),
    })
    public ResponseEntity<ProjectInfoResponseDto> createProject(@RequestPart(value="file", required=false) MultipartFile file, @RequestPart(value="key", required=true) ProjectCreateRequestDto dto) {
        return new ResponseEntity<>(projectService.create(file, dto), HttpStatus.OK);
    }

    @PutMapping("/{projectId}")
    @ApiOperation(value = "프로젝트 수정", notes = "<strong>받은 프로젝트 정보</strong>를 사용해서 프로젝트를 수정한다.")
    @ApiResponses({
        @ApiResponse(code = 200, message = "수정된 정보"),
        @ApiResponse(code = 400, message = "DEVELOPER_COUNT_OVER\nPLANNER_COUNT_OVER\nDESIGNER_COUNT_OVER"
            + "\nDEVELOPER_COUNT_BELOW_ZERO\nPLANNER_COUNT_BELOW_ZERO\nDESIGNER_COUNT_BELOW_ZERO"),
        @ApiResponse(code = 404, message = "CITY_NOT_FOUND\nMEMBER_NOT_FOUND\nCLUB_NOT_FOUND\nFILE_NOT_FOUND"
            + "\nTECHSTACK_NOT_FOUND\nPROJECT_NOT_FOUND\nMEMBER_PROJECT_NOT_FOUND\nLEVEL_NOT_FOUND\nROLE_NOT_FOUND"),
    })
    public ResponseEntity<ProjectInfoResponseDto> updateProject(@Valid @RequestBody ProjectUpdateRequestDto dto,
        @PathVariable("projectId") Long projectId) {
        return new ResponseEntity<>(projectService.update(projectId, dto), HttpStatus.OK);
    }

    @DeleteMapping("/{projectId}")
    @ApiOperation(value = "프로젝트 삭제", notes = "<strong>받은 프로젝트 Id</strong>로 프로젝트와 포함된 멤버관계를 삭제한다.")
    @ApiResponses({
        @ApiResponse(code = 200, message = "삭제되었습니다."),
        @ApiResponse(code = 401, message = "UNAUTHORIZED_CHANGE"),
        @ApiResponse(code = 404, message = "MEMBER_NOT_FOUND\nPROJECT_NOT_FOUND\nMEMBER_PROJECT_NOT_FOUND"),
    })
    public ResponseEntity<String> deleteProject(@PathVariable("projectId") Long projectId) {
        return new ResponseEntity<>("삭제되었습니다.", projectService.delete(projectId));
    }

    @DeleteMapping("/{projectId}/member")
    @ApiOperation(value = "프로젝트 탈퇴", notes = "<strong>받은 프로젝트 id</strong>로 프로젝트에서 탈퇴한다.")
    @ApiResponses({
        @ApiResponse(code = 200, message = "성공"),
        @ApiResponse(code = 400, message = "DEVELOPER_COUNT_BELOW_ZERO\nPLANNER_COUNT_BELOW_ZERO\nDESIGNER_COUNT_BELOW_ZERO\nHOST_CANNOT_LEAVE"),
        @ApiResponse(code = 404, message = "MEMBER_NOT_FOUND\nPROJECT_NOT_FOUND\nMEMBER_PROJECT_NOT_FOUND\nROLE_NOT_FOUND"),
    })
    public ResponseEntity<String> removeMe(@PathVariable("projectId") Long projectId) {
        return new ResponseEntity<>("탈퇴되었습니다.", projectService.removeMe(projectId));
    }

    @DeleteMapping("/{projectId}/{memberId}")
    @ApiOperation(value = "프로젝트 추방", notes = "<strong>받은 프로젝트 id와 탈퇴시킬 멤버의 id</strong>로 프로젝트에서 탈퇴시킨다.")
    @ApiResponses({
        @ApiResponse(code = 200, message = "성공"),
        @ApiResponse(code = 400, message = "DEVELOPER_COUNT_BELOW_ZERO\nPLANNER_COUNT_BELOW_ZERO\nDESIGNER_COUNT_BELOW_ZERO"
            + "\nONLY_CAN_REMOVE_COMMON\nCOMMON_MEMBER_CANNOT_REMOVE"),
        @ApiResponse(code = 404, message = "MEMBER_NOT_FOUND\nPROJECT_NOT_FOUND\nMEMBER_PROJECT_NOT_FOUND\nROLE_NOT_FOUND"),
    })
    public ResponseEntity<String> removeMember(@PathVariable("projectId") Long projectId,
        @PathVariable("memberId") Long memberId) {
        return new ResponseEntity<>("처리되었습니다.", projectService.removeMember(projectId, memberId));
    }

    @GetMapping("/{projectId}")
    @ApiOperation(value = "프로젝트 상세정보 조회",
        notes = "<strong>받은 프로젝트 Id</strong>로 해당 프로젝트를 조회 + 전체 기술스택, 역할별 인원 닉네임, 전체 지역 정보, 포함 인원 등")
    @ApiResponses({
        @ApiResponse(code = 200, message = "성공"),
        @ApiResponse(code = 404, message = "MEMBER_NOT_FOUND\nPROJECT_NOT_FOUND\nMEMBER_PROJECT_NOT_FOUND"),
    })
    public ResponseEntity<ProjectInfoResponseDto> getOneProject(
        @PathVariable("projectId") Long projectId) {
        return new ResponseEntity<>(projectService.getOneProject(projectId), HttpStatus.OK);
    }

    @GetMapping("/one/simple/{projectId}")
    @ApiOperation(value = "프로젝트 간편 조회",
        notes = "<strong>받은 프로젝트 Id</strong>로 프로젝트 관리의 간편 정보 조회")
    @ApiResponses({
        @ApiResponse(code = 200, message = "성공"),
        @ApiResponse(code = 404, message = "MEMBER_NOT_FOUND\nPROJECT_NOT_FOUND\nMEMBER_PROJECT_NOT_FOUND"),
    })
    public ResponseEntity<ProjectSimpleInfoResponseDto> getOneSimpleProject(
        @PathVariable("projectId") Long projectId) {
        return new ResponseEntity<>(projectService.getOneSimpleProject(projectId), HttpStatus.OK);
    }

    @GetMapping("/member/{projectId}")
    @ApiOperation(value = "프로젝트 구성원", notes = "<strong>받은 프로젝트 Id</strong>로 프로젝트 관리의 구성원 조회")
    @ApiResponses({
        @ApiResponse(code = 200, message = "성공"),
        @ApiResponse(code = 404, message = "MEMBER_NOT_FOUND\nPROJECT_NOT_FOUND\nMEMBER_PROJECT_NOT_FOUND"),
    })
    public ResponseEntity<List<ProjectMemberResponseDto>> getMemberInProject(@PathVariable("projectId") Long projectId) {
        return new ResponseEntity<>(projectService.memberInProject(projectId), HttpStatus.OK);
    }

//    @GetMapping("/recommendation")
//    @ApiOperation(value = "추천 프로젝트 조회", notes = "<strong>받은 프로젝트 Id</strong>로 해당 멤버가 속한 프로젝트 정보 조회")
//    @ApiResponses({
//        @ApiResponse(code = 200, message = "성공"),
//    })
//    public ResponseEntity<List<ProjectSimpleInfoResponseDto>> projectInMember(
//        @PageableDefault(size = 10) @SortDefault(sort = "createDate", direction = Sort.Direction.DESC) Pageable pageable) {
//        return ResponseEntity.ok(projectService.getRecommendationProject(pageable));
//    }

    @GetMapping
    @ApiOperation(value = "모든 프로젝트 조회", notes = "프로젝트 종료가 아닌 // 모집 중 // 전체 공개 // 를 만족하는 프로젝트들을 작성일 기준 내림차순으로 받는다")
    public ResponseEntity<Page<ProjectSimpleInfoResponseDto>> getAllProject(
        @PageableDefault(size = 10) @SortDefault(sort = "createDate", direction = Sort.Direction.DESC) Pageable pageable) {
        return ResponseEntity.ok(projectService.getAllProject(pageable));
    }

    @PutMapping("/role/{projectId}/{memberId}/{role}")
    @ApiOperation(value = "역할 변경", notes = "<strong>받은 프로젝트 id와 변경 시킬 멤버의 id와 변경하고자하는 role을 받아</strong>로 역할을 변경시킨다.")
    @ApiResponses({
        @ApiResponse(code = 200, message = "성공"),
        @ApiResponse(code = 400, message = "DEVELOPER_COUNT_OVER\nPLANNER_COUNT_OVER\nDESIGNER_COUNT_OVER\nDEVELOPER_COUNT_BELOW_ZERO"
            + "\nPLANNER_COUNT_BELOW_ZERO\nDESIGNER_COUNT_BELOW_ZERO"),
        @ApiResponse(code = 404, message = "MEMBER_NOT_FOUND\nPROJECT_NOT_FOUND\nMEMBER_PROJECT_NOT_FOUND\nROLE_NOT_FOUND"),
    })
    public ResponseEntity<String> changeRole(@PathVariable("projectId") Long projectId,
        @PathVariable("memberId") Long memberId, @PathVariable("role") String role) {
        return new ResponseEntity<>("역할이 변경되었습니다.", projectService.changeRole(projectId, memberId, role));
    }

    @PutMapping("/authority/{projectId}/{memberId}/{authority}")
    @ApiOperation(value = "권한 변경", notes = "<strong>받은 프로젝트 id와 변경 시킬 멤버의 id와 변경하고자하는 authority을 받아</strong>로 권한을 변경시킨다.")
    @ApiResponses({
        @ApiResponse(code = 200, message = "성공"),
        @ApiResponse(code = 401, message = "UNAUTHORIZED_CHANGE"),
        @ApiResponse(code = 404, message = "GROUP_AUTHORITY_NOT_FOUND\nMEMBER_NOT_FOUND\nPROJECT_NOT_FOUND\nMEMBER_PROJECT_NOT_FOUND\nROLE_NOT_FOUND"),
    })
    public ResponseEntity<String> changeAuthority(@PathVariable("projectId") Long projectId,
        @PathVariable("memberId") Long memberId, @PathVariable("authority") String authority) {
        return new ResponseEntity<>("권한이 변경되었습니다.", projectService.changeAuthority(projectId, memberId, authority));
    }

    @PutMapping(value = "/cover-pic/{projectId}", consumes = { MediaType.MULTIPART_FORM_DATA_VALUE })
    @ApiOperation(value = "사진 변경", notes = "<strong>받은 프로젝트 id와 넣을 uuid을 받아</strong>로 사진을 변경시킨다.")
    @ApiResponses({
        @ApiResponse(code = 200, message = "성공"),
        @ApiResponse(code = 404, message = "PROJECT_NOT_FOUND"),
    })
    public ResponseEntity<DBFileDto> changeCoverPic(@RequestPart(value="file", required=false) MultipartFile file, @PathVariable("projectId") Long projectId) {
        return new ResponseEntity<>(projectService.changeCoverPic(file, projectId), HttpStatus.OK);
    }

    @PutMapping("/view-count/{projectId}")
    @ApiOperation(value = "조회 수 증가", notes = "<strong>받은 프로젝트 id</strong>로 조회수를 증가시킨다.")
    @ApiResponses({
        @ApiResponse(code = 200, message = "처리되었습니다."),
        @ApiResponse(code = 404, message = "PROJECT_NOT_FOUND"),
    })
    public ResponseEntity<String> plusViewCount(@PathVariable("projectId") Long projectId) {
        return new ResponseEntity<>("처리되었습니다.", projectService.plusViewCount(projectId));
    }

    @GetMapping("/cover-pic/{projectId}")
    @ApiOperation(value = "프로젝트 사진 정보", notes = "<strong>받은 프로젝트 id</strong>로 프로젝트 사진을 가져온다.")
    @ApiResponses({
        @ApiResponse(code = 200, message = "다운로드 uri"),
        @ApiResponse(code = 404, message = "PROJECT_NOT_FOUND"),
    })
    public ResponseEntity<DBFileDto> getCoverPicUri(@PathVariable("projectId") Long projectId) {
        return new ResponseEntity<>(projectService.getCoverPicUri(projectId), HttpStatus.OK);
    }

    @GetMapping("/authority/{projectId}")
    @ApiOperation(value = "현 사용자의 권한 정보", notes = "<strong>받은 프로젝트 id</strong>로 현 사용자에 대한 권한을 확인한다.")
    @ApiResponses({
        @ApiResponse(code = 200, message = "소유자, 관리자, 게스트"),
        @ApiResponse(code = 404, message = "MEMBER_NOT_FOUND\nPROJECT_NOT_FOUND\nMEMBER_PROJECT_NOT_FOUND"),
    })
    public ResponseEntity<String> getMemberAuthority(@PathVariable("projectId") Long projectId) {
        return new ResponseEntity<>(projectService.getMemberAuthority(projectId), HttpStatus.OK);
    }

}
