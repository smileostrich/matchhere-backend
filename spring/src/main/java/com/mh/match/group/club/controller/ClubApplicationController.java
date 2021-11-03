package com.mh.match.group.club.controller;

import com.mh.match.group.club.service.ClubService;
import com.mh.match.group.club.dto.request.ClubApplicationRequestDto;
import com.mh.match.group.club.dto.response.ClubFormInfoResponseDto;
import com.mh.match.group.club.dto.response.ClubFormSimpleInfoResponseDto;
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
@RequestMapping("/clubapplication")
public class ClubApplicationController {

    private final ClubService clubService;

    @GetMapping("/check-apply/{clubId}")
    @ApiOperation(value = "신청서 생성 가능 여부", notes = "멤버가 클럽에 신청 가능한지 여부(클럽 종료, 삭제, 모집, 이미 가입된 멤버인지 여부 확인)")
    @ApiResponses({
            @ApiResponse(code = 200, message = "신청 가능합니다."),
            @ApiResponse(code = 400, message = "CANNOT_APPLY\nALREADY_JOIN"),
            @ApiResponse(code = 404, message = "CLUB_NOT_FOUND\nMEMBER_NOT_FOUND"),
    })
    public ResponseEntity<Boolean> checkCanApply(@PathVariable("clubId") Long clubId) {
        return ResponseEntity.ok(clubService.checkCanApply(clubId));
    }

    @PostMapping("/{clubId}")
    @ApiOperation(value = "클럽 가입 신청", notes = "<strong>받은 신청서 정보로</strong>를 사용해서 클럽에 신청을 한다")
    @ApiResponses({
            @ApiResponse(code = 200, message = "신청서 정보"),
            @ApiResponse(code = 404, message = "CLUB_NOT_FOUND\nMEMBER_NOT_FOUND"),
    })
    public ResponseEntity<ClubFormInfoResponseDto> applyClub(
            @PathVariable("clubId") Long clubId, @RequestBody ClubApplicationRequestDto dto)
            throws Exception {
        return ResponseEntity.ok(clubService.applyClub(clubId, dto));
    }

    @PostMapping("/approval/{clubId}/{memberId}")
    @ApiOperation(value = "클럽 가입 승인", notes = "<strong>받은 신청서 Id</strong>를 사용해서 해당 멤버를 가입 승인한다.")
    @ApiResponses({
            @ApiResponse(code = 200, message = "OK"),
            @ApiResponse(code = 400, message = "MEMBER_COUNT_OVER"),
            @ApiResponse(code = 401, message = "UNAUTHORIZED_SELECT"),
            @ApiResponse(code = 404, message = "CLUB_NOT_FOUND\nMEMBER_CLUB_NOT_FOUND\nMEMBER_NOT_FOUND\nAPPLIY_FORM_NOT_FOUND"),
    })
    public ResponseEntity<HttpStatus> approval(@PathVariable("clubId") Long clubId,
                                               @PathVariable("memberId") Long memberId) {
        return ResponseEntity.ok(clubService.approval(clubId, memberId));
    }

    @DeleteMapping("{clubId}/{memberId}")
    @ApiOperation(value = "신청서 삭제", notes = "<strong>받은 신청서 Id</strong>를 사용해서 해당 신청서를 제거한다.")
    @ApiResponses({
            @ApiResponse(code = 200, message = "OK"),
            @ApiResponse(code = 401, message = "UNAUTHORIZED_SELECT"),
            @ApiResponse(code = 404, message = "CLUB_NOT_FOUND\nMEMBER_CLUB_NOT_FOUND\nMEMBER_NOT_FOUND\nAPPLIY_FORM_NOT_FOUND"),
    })
    public ResponseEntity<HttpStatus> reject(@PathVariable("clubId") Long clubId,
                                             @PathVariable("memberId") Long memberId) {
        return ResponseEntity.ok(clubService.reject(clubId, memberId));
    }

    @GetMapping("/all/{clubId}")
    @ApiOperation(value = "특정 클럽 모든 신청서 조회", notes = "특정 클럽의 모든 신청서 리스트를 작성일 기준 내림차순으로 받는다")
    @ApiResponses({
            @ApiResponse(code = 200, message = "신청서 조회"),
            @ApiResponse(code = 401, message = "UNAUTHORIZED_SELECT"),
            @ApiResponse(code = 404, message = "CLUB_NOT_FOUND\nMEMBER_NOT_FOUND\nMEMBER_CLUB_NOT_FOUND"),
    })
    public ResponseEntity<List<ClubFormSimpleInfoResponseDto>> getAllClubForm(
            @PathVariable("clubId") Long clubId) {
        return ResponseEntity.ok(clubService.getAllClubForm(clubId));
    }

    @GetMapping("/one/{clubId}/{memberId}")
    @ApiOperation(value = "특정 신청서 조회", notes = "특정 신청서를 상세 조회한다.")
    @ApiResponses({
            @ApiResponse(code = 200, message = "신청서 조회"),
            @ApiResponse(code = 404, message = "CLUB_NOT_FOUND\nMEMBER_NOT_FOUND\nAPPLIY_FORM_NOT_FOUND"),
    })
    public ResponseEntity<ClubFormInfoResponseDto> getOneClubForm(
            @PathVariable("clubId") Long clubId, @PathVariable("memberId") Long memberId)
            throws Exception {
        return ResponseEntity.ok(clubService.getOneClubForm(clubId, memberId));
    }

}