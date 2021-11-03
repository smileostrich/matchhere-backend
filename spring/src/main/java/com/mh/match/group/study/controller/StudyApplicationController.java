package com.mh.match.group.study.controller;

import com.mh.match.group.study.dto.request.StudyApplicationRequestDto;
import com.mh.match.group.study.dto.response.StudyFormInfoResponseDto;
import com.mh.match.group.study.dto.response.StudyFormSimpleInfoResponseDto;
import com.mh.match.group.study.service.StudyService;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import java.util.List;
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
@RequestMapping("/studyapplication")
public class StudyApplicationController {

    private final StudyService studyService;

    @GetMapping("/check-apply/{studyId}")
    @ApiOperation(value = "신청서 생성 가능 여부", notes = "멤버가 스터디에 신청 가능한지 여부(스터디 종료, 삭제, 모집, 이미 가입된 멤버인지 여부 확인)")
    @ApiResponses({
        @ApiResponse(code = 200, message = "신청 가능합니다."),
        @ApiResponse(code = 400, message = "CANNOT_APPLY\nALREADY_JOIN"),
        @ApiResponse(code = 404, message = "STUDY_NOT_FOUND\nMEMBER_NOT_FOUND"),
    })
    public ResponseEntity<Boolean> checkCanApply(@PathVariable("studyId") Long studyId) {
        return ResponseEntity.ok(studyService.checkCanApply(studyId));
    }

    @PostMapping("/{studyId}")
    @ApiOperation(value = "스터디 가입 신청", notes = "<strong>받은 신청서 정보로</strong>를 사용해서 스터디에 신청을 한다")
    @ApiResponses({
        @ApiResponse(code = 200, message = "신청서 정보"),
        @ApiResponse(code = 404, message = "STUDY_NOT_FOUND\nMEMBER_NOT_FOUND"),
    })
    public ResponseEntity<StudyFormInfoResponseDto> applyStudy(
        @PathVariable("studyId") Long studyId, @RequestBody StudyApplicationRequestDto dto)
        throws Exception {
        return ResponseEntity.ok(studyService.applyStudy(studyId, dto));
    }

    @PostMapping("/approval/{studyId}/{memberId}")
    @ApiOperation(value = "스터디 가입 승인", notes = "<strong>받은 신청서 Id</strong>를 사용해서 해당 멤버를 가입 승인한다.")
    @ApiResponses({
        @ApiResponse(code = 200, message = "OK"),
        @ApiResponse(code = 400, message = "MEMBER_COUNT_OVER"),
        @ApiResponse(code = 401, message = "UNAUTHORIZED_SELECT"),
        @ApiResponse(code = 404, message = "STUDY_NOT_FOUND\nMEMBER_STUDY_NOT_FOUND\nMEMBER_NOT_FOUND\nAPPLIY_FORM_NOT_FOUND"),
    })
    public ResponseEntity<HttpStatus> approval(@PathVariable("studyId") Long studyId,
        @PathVariable("memberId") Long memberId) {
        return ResponseEntity.ok(studyService.approval(studyId, memberId));
    }

    @DeleteMapping("{studyId}/{memberId}")
    @ApiOperation(value = "신청서 삭제", notes = "<strong>받은 신청서 Id</strong>를 사용해서 해당 신청서를 제거한다.")
    @ApiResponses({
        @ApiResponse(code = 200, message = "OK"),
        @ApiResponse(code = 401, message = "UNAUTHORIZED_SELECT"),
        @ApiResponse(code = 404, message = "STUDY_NOT_FOUND\nMEMBER_STUDY_NOT_FOUND\nMEMBER_NOT_FOUND\nAPPLIY_FORM_NOT_FOUND"),
    })
    public ResponseEntity<HttpStatus> reject(@PathVariable("studyId") Long studyId,
        @PathVariable("memberId") Long memberId) {
        return ResponseEntity.ok(studyService.reject(studyId, memberId));
    }

    @GetMapping("/all/{studyId}")
    @ApiOperation(value = "특정 스터디 모든 신청서 조회", notes = "특정 스터디의 모든 신청서 리스트를 작성일 기준 내림차순으로 받는다")
    @ApiResponses({
        @ApiResponse(code = 200, message = "신청서 조회"),
        @ApiResponse(code = 401, message = "UNAUTHORIZED_SELECT"),
        @ApiResponse(code = 404, message = "STUDY_NOT_FOUND\nMEMBER_NOT_FOUND\nMEMBER_STUDY_NOT_FOUND"),
    })
    public ResponseEntity<List<StudyFormSimpleInfoResponseDto>> getAllStudyForm(
        @PathVariable("studyId") Long studyId) {
        return ResponseEntity.ok(studyService.getAllStudyForm(studyId));
    }

    @GetMapping("/one/{studyId}/{memberId}")
    @ApiOperation(value = "특정 신청서 조회", notes = "특정 신청서를 상세 조회한다.")
    @ApiResponses({
        @ApiResponse(code = 200, message = "신청서 조회"),
        @ApiResponse(code = 404, message = "STUDY_NOT_FOUND\nMEMBER_NOT_FOUND\nAPPLIY_FORM_NOT_FOUND"),
    })
    public ResponseEntity<StudyFormInfoResponseDto> getOneStudyForm(
        @PathVariable("studyId") Long studyId, @PathVariable("memberId") Long memberId)
        throws Exception {
        return ResponseEntity.ok(studyService.getOneStudyForm(studyId, memberId));
    }

}
