package com.mh.match.group.project.controller;

import com.mh.match.group.project.dto.request.ProjectApplicationRequestDto;
import com.mh.match.group.project.dto.response.ProjectFormInfoResponseDto;
import com.mh.match.group.project.dto.response.ProjectFormSimpleInfoResponseDto;
import com.mh.match.group.project.service.ProjectService;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import java.util.List;
import javax.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/projectapplication")
public class ProjectApplicationController {

    private final ProjectService projectService;

    @GetMapping("/check/{projectId}")
    @ApiOperation(value = "신청서 생성 가능 여부", notes = "멤버가 프로젝트에 신청 가능한지 여부")
    @ApiResponses({
        @ApiResponse(code = 200, message = "신청 가능합니다."),
        @ApiResponse(code = 400, message = "CANNOT_APPLY\nALREADY_JOIN"),
        @ApiResponse(code = 404, message = "PROJECT_NOT_FOUND\nMEMBER_NOT_FOUND"),
    })
    public ResponseEntity<String> checkForApply(@PathVariable("projectId") Long projectId) {
        return new ResponseEntity<>("신청 가능합니다", projectService.checkCanApply(projectId));
    }

    @PostMapping("/{projectId}")
    @ApiOperation(value = "프로젝트 가입 신청", notes = "<strong>받은 신청서 정보로</strong>를 사용해서 프로젝트에 신청을 한다")
    @ApiResponses({
        @ApiResponse(code = 200, message = "신청서 정보"),
        @ApiResponse(code = 400, message = "ALREADY_APPLY"),
        @ApiResponse(code = 404, message = "PROJECT_NOT_FOUND\nMEMBER_NOT_FOUND"),
    })
    public ResponseEntity<ProjectFormInfoResponseDto> createForm(@PathVariable("projectId") Long projectId,
        @Valid @RequestBody ProjectApplicationRequestDto dto) {
        return new ResponseEntity<>(projectService.applyProject(projectId, dto), HttpStatus.OK);
    }

    @PostMapping("/approval/{projectId}/{memberId}")
    @ApiOperation(value = "프로젝트 가입 승인", notes = "<strong>받은 신청서 Id</strong>를 사용해서 해당 멤버를 가입 승인한다.")
    @ApiResponses({
        @ApiResponse(code = 200, message = "가입 처리되었습니다."),
        @ApiResponse(code = 400, message = "DEVELOPER_COUNT_OVER\nPLANNER_COUNT_OVER\nDESIGNER_COUNT_OVER"),
        @ApiResponse(code = 404, message = "PROJECT_NOT_FOUND\nMEMBER_NOT_FOUND\nAPPLIY_FORM_NOT_FOUND"),
    })
    public ResponseEntity<String> approval(@PathVariable("projectId") Long projectId,
        @PathVariable("memberId") Long memberId) {
        return new ResponseEntity<>("가입 처리되었습니다.", projectService.approval(projectId, memberId));
    }

    @DeleteMapping("{projectId}/{memberId}")
    @ApiOperation(value = "가입 거절", notes = "<strong>받은 신청서 Id</strong>를 사용해서 해당 신청서를 제거한다.")
    @ApiResponses({
        @ApiResponse(code = 200, message = "신청서가 거절되었습니다."),
        @ApiResponse(code = 404, message = "PROJECT_NOT_FOUND\nMEMBER_NOT_FOUND\nAPPLIY_FORM_NOT_FOUND"),
    })
    public ResponseEntity<String> reject(@PathVariable("projectId") Long projectId,
        @PathVariable("memberId") Long memberId) {
        return new ResponseEntity<>("신청서가 거절되었습니다.", projectService.reject(projectId, memberId));
    }

    @GetMapping("/all/{projectId}")
    @ApiOperation(value = "특정 프로젝트 신청서  조회", notes = "특정 프로젝트의 신청서 리스트를 작성일 기준 내림차순으로 받는다")
    @ApiResponses({
        @ApiResponse(code = 200, message = "신청되었습니다."),
        @ApiResponse(code = 401, message = "UNAUTHORIZED_SELECT"),
        @ApiResponse(code = 404, message = "PROJECT_NOT_FOUND\nMEMBER_NOT_FOUND\nMEMBER_PROJECT_NOT_FOUND"),
    })
    public ResponseEntity<List<ProjectFormSimpleInfoResponseDto>> allProjectForm(@PathVariable("projectId") Long projectId) {
        return new ResponseEntity<>(projectService.allProjectForm(projectId), HttpStatus.OK);
    }

    @GetMapping("/one/{projectId}/{memberId}")
    @ApiOperation(value = "특정 신청서 조회", notes = "특정 신청서를 상세 조회한다.")
    @ApiResponses({
        @ApiResponse(code = 200, message = "성공"),
        @ApiResponse(code = 404, message = "PROJECT_NOT_FOUND\nMEMBER_NOT_FOUND\nAPPLIY_FORM_NOT_FOUND"),
    })
    public ResponseEntity<ProjectFormInfoResponseDto> oneProjectForm(
        @PathVariable("projectId") Long projectId, @PathVariable("memberId") Long memberId) {
        return new ResponseEntity<>(projectService.oneProjectForm(projectId, memberId), HttpStatus.OK);
    }

}
