package com.mh.match.group.study.controller;

import com.mh.match.group.study.dto.response.*;
import com.mh.match.group.study.service.StudyService;
import com.mh.match.s3.dto.DBFileDto;
import com.mh.match.group.study.dto.request.StudyCreateRequestDto;
import com.mh.match.group.study.dto.request.StudyUpdateRequestDto;
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
@RequestMapping("/study")
public class StudyController {

    private final StudyService studyService;

    @GetMapping("/myclublist")
    @ApiOperation(value = "스터디 생성을 위한 정보", notes = "스터디 생성을 위해 사용자의 클럽 정보를 조회")
    @ApiResponses({
        @ApiResponse(code = 200, message = "클럽 정보 조회"),
        @ApiResponse(code = 404, message = "MEMBER_NOT_FOUND"),
    })
    public ResponseEntity<StudyInfoForCreateResponseDto> getInfoForCreate() {
        return ResponseEntity.ok(studyService.getInfoForCreate());
    }

    @GetMapping("/infoforupdate/{studyId}")
    @ApiOperation(value = "스터디 업데이트를 위한 정보",
        notes = "<strong>받은 스터디 id</strong>로 해당 스터디 정보 + 수정을 위한 정보(사용자 클럽 리스트, 지역, 상태 리스트 등")
    @ApiResponses({
        @ApiResponse(code = 200, message = "정보 조회"),
        @ApiResponse(code = 401, message = "UNAUTHORIZED_CHANGE"),
        @ApiResponse(code = 404, message = "MEMBER_NOT_FOUND\nSTUDY_NOT_FOUND"),
    })
    public ResponseEntity<StudyInfoForUpdateResponseDto> getInfoForUpdate(@PathVariable("studyId") Long studyId) {
        return ResponseEntity.ok(studyService.getInfoForUpdateStudy(studyId));
    }

    @PostMapping(value = "", consumes = { MediaType.APPLICATION_JSON_VALUE, MediaType.MULTIPART_FORM_DATA_VALUE })
    @ApiOperation(value = "스터디 생성", notes = "<strong>받은 스터디 정보</strong>를 사용해서 스터디을 생성한다.")
    @ApiResponses({
        @ApiResponse(code = 200, message = "생성한 스터디 정보"),
        @ApiResponse(code = 400, message = "MEMBER_COUNT_OVER\nMEMBER_COUNT_BELOW_ZERO"),
        @ApiResponse(code = 404, message = "CITY_NOT_FOUND\nMEMBER_NOT_FOUND\nCLUB_NOT_FOUND\nFILE_NOT_FOUND\nAUTHORITY_NOT_FOUND"),
    })
    public ResponseEntity<StudyInfoResponseDto> create(@RequestPart(value="file", required=false) MultipartFile file, @RequestPart(value="key", required=true) StudyCreateRequestDto dto) {
        return ResponseEntity.ok(studyService.create(file, dto));
    }

    @PutMapping("/{studyId}")
    @ApiOperation(value = "스터디 수정", notes = "<strong>받은 스터디 정보</strong>를 사용해서 스터디를 수정한다.")
    @ApiResponses({
        @ApiResponse(code = 200, message = "수정된 정보"),
        @ApiResponse(code = 400, message = "MEMBER_COUNT_OVER"),
        @ApiResponse(code = 401, message = "UNAUTHORIZED_CHANGE"),
        @ApiResponse(code = 404, message = "CITY_NOT_FOUND\nMEMBER_NOT_FOUND\nCLUB_NOT_FOUND\nSTUDY_NOT_FOUND\nMEMBER_STUDY_NOT_FOUND")
    })
    public ResponseEntity<StudyInfoResponseDto> update(@PathVariable("studyId") Long studyId,
        @Valid @RequestBody StudyUpdateRequestDto dto) {
        return ResponseEntity.ok(studyService.update(studyId, dto));
    }

    @PutMapping(value = "/cover-pic/{studyId}", consumes = { MediaType.MULTIPART_FORM_DATA_VALUE })
    @ApiOperation(value = "사진 변경", notes = "<strong>받은 스터디 id와 넣을 uuid을 받아</strong>로 사진을 변경시킨다.")
    @ApiResponses({
        @ApiResponse(code = 200, message = "성공"),
        @ApiResponse(code = 404, message = "STUDY_NOT_FOUND\nFILE_NOT_FOUND"),
    })
    public ResponseEntity<DBFileDto> changeCoverPic(@RequestPart(value="file", required=false) MultipartFile file, @PathVariable("studyId") Long studyId) {
        return new ResponseEntity<>(studyService.changeCoverPic(file, studyId), HttpStatus.OK);
    }

    @PutMapping("/view-count/{studyId}")
    @ApiOperation(value = "조회 수 증가", notes = "<strong>받은 스터디 id</strong>로 조회수를 증가시킨다.")
    @ApiResponses({
        @ApiResponse(code = 200, message = "처리되었습니다."),
        @ApiResponse(code = 404, message = "STUDY_NOT_FOUND"),
    })
    public ResponseEntity<String> plusViewCount(@PathVariable("studyId") Long studyId) {
        return new ResponseEntity<>("처리되었습니다.", studyService.plusViewCount(studyId));
    }

    @PutMapping("/authority/{studyId}/{memberId}/{authority}")
    @ApiOperation(value = "권한 변경", notes = "<strong>받은 스터디 id와 변경 시킬 멤버의 id와 변경하고자하는 authority을 받아</strong>로 권한을 변경시킨다.")
    @ApiResponses({
        @ApiResponse(code = 200, message = "권한이 변경되었습니다."),
        @ApiResponse(code = 401, message = "UNAUTHORIZED_CHANGE"),
        @ApiResponse(code = 404, message = "GROUP_AUTHORITY_NOT_FOUND\nMEMBER_NOT_FOUND\nSTUDY_NOT_FOUND\nMEMBER_STUDY_NOT_FOUND\nROLE_NOT_FOUND"),
    })
    public ResponseEntity<String> changeAuthority(@PathVariable("studyId") Long studyId,
        @PathVariable("memberId") Long memberId, @PathVariable("authority") String authority) {
        return new ResponseEntity<>("권한이 변경되었습니다.", studyService.changeAuthority(studyId, memberId, authority));
    }

    @DeleteMapping("/{studyId}")
    @ApiOperation(value = "스터디 삭제", notes = "<strong>받은 스터디 Id</strong>로 스터디 관련 정보(멤버 관계, 사진, 주제)를 삭제한다.")
    @ApiResponses({
        @ApiResponse(code = 200, message = "삭제되었습니다."),
        @ApiResponse(code = 401, message = "UNAUTHORIZED_CHANGE"),
        @ApiResponse(code = 404, message = "MEMBER_NOT_FOUND\nSTUDY_NOT_FOUND\nMEMBER_STUDY_NOT_FOUND\nFILE_NOT_FOUND"),
    })
    public ResponseEntity<HttpStatus> delete(@PathVariable("studyId") Long studyId) {
        return ResponseEntity.ok(studyService.delete(studyId));
    }

    @DeleteMapping("/{studyId}/member")
    @ApiOperation(value = "스터디 탈퇴", notes = "<strong>받은 스터디 id</strong>로 스터디에서 탈퇴한다.")
    @ApiResponses({
        @ApiResponse(code = 200, message = "탈퇴되었습니다."),
        @ApiResponse(code = 400, message = "MEMBER_COUNT_BELOW_ZERO\nHOST_CANNOT_LEAVE"),
        @ApiResponse(code = 404, message = "MEMBER_NOT_FOUND\nSTUDY_NOT_FOUND\nMEMBER_STUDY_NOT_FOUND"),
    })
    public ResponseEntity<String> removeMe(@PathVariable("studyId") Long studyId) {
        return new ResponseEntity<>("탈퇴되었습니다.", studyService.removeMe(studyId));
    }

    @DeleteMapping("/{studyId}/{memberId}")
    @ApiOperation(value = "스터디 추방", notes = "<strong>받은 스터디 id와 탈퇴시킬 멤버의 id</strong>로 스터디에서 탈퇴시킨다.")
    @ApiResponses({
        @ApiResponse(code = 200, message = "처리되었습니다."),
        @ApiResponse(code = 400, message = "MEMBER_COUNT_BELOW_ZERO\nONLY_CAN_REMOVE_COMMON\nCOMMON_MEMBER_CANNOT_REMOVE"),
        @ApiResponse(code = 404, message = "MEMBER_NOT_FOUND\nSTUDY_NOT_FOUND\nMEMBER_STUDY_NOT_FOUND"),
    })
    public ResponseEntity<String> removeMember(@PathVariable("studyId") Long studyId,
        @PathVariable("memberId") Long memberId) {
        return new ResponseEntity<>("처리되었습니다.", studyService.removeMember(studyId, memberId));
    }

    @GetMapping
    @ApiOperation(value = "모든 스터디 조회", notes = "스터디 종료가 아닌 // 모집 중 // 전체 공개 // 를 만족하는 스터디들을 작성일 기준 내림차순으로 받는다")
    @ApiResponses({
        @ApiResponse(code = 200, message = "페이징된 스터디 조회"),
    })
    public ResponseEntity<Page<StudySimpleInfoResponseDto>> getAllStudy(
        @PageableDefault(size = 10) @SortDefault(sort = "createdDate", direction = Sort.Direction.DESC) Pageable pageable) {
        return ResponseEntity.ok(studyService.getAllStudy(pageable));
    }

//    @GetMapping("/recommend")
//    @ApiOperation(value = "모든 스터디 조회", notes = "추천 하는 스터디들을 리턴한다")
//    public ResponseEntity<Page<StudyInfoResponseDto>> getAllStudyWithRecommend(@SortDefault.SortDefaults({
////            @SortDefault(sort = "createDate", direction= Sort.Direction.DESC),
//            @SortDefault(sort = "period", direction = Sort.Direction.DESC),
//            @SortDefault(sort = "maxCount", direction = Sort.Direction.DESC)
//    }) @PageableDefault(size = 10) Pageable pageable) throws Exception {
//        return ResponseEntity.ok(studyService.getAllStudy(pageable));
//    }

    @GetMapping("/{studyId}")
    @ApiOperation(value = "스터디 상세정보 조회",
        notes = "<strong>받은 스터디 id</strong>로 해당 스터디 정보 + 수정을 위한 정보(사용자 클럽 리스트, 지역, 상태 리스트 등")
    @ApiResponses({
        @ApiResponse(code = 200, message = "스터디 상세 정보"),
        @ApiResponse(code = 404, message = "STUDY_NOT_FOUND\nDELETED_STUDY"),
    })
    public ResponseEntity<StudyInfoResponseDto> getOneStudy(@PathVariable("studyId") Long studyId) {
        return ResponseEntity.ok(studyService.getOneStudy(studyId));
    }

    @GetMapping("/one/simple/{studyId}")
    @ApiOperation(value = "스터디 간편정보 조회", notes = "<strong>받은 스터디 id</strong>로 스터디 관리 화면의 간편 정보 조회")
    @ApiResponses({
        @ApiResponse(code = 200, message = "스터디 간편 정보"),
        @ApiResponse(code = 404, message = "STUDY_NOT_FOUND\nDELETED_STUDY"),
    })
    public ResponseEntity<StudySimpleInfoResponseDto> getOneSimpleStudy(@PathVariable("studyId") Long studyId) {
        return ResponseEntity.ok(studyService.getOneSimpleStudy(studyId));
    }

    @GetMapping("/authority/{studyId}")
    @ApiOperation(value = "현 사용자의 권한 정보", notes = "<strong>받은 스터디 id</strong>로 현 사용자에 대한 권한을 확인한다.")
    @ApiResponses({
        @ApiResponse(code = 200, message = "소유자, 관리자, 게스트"),
        @ApiResponse(code = 404, message = "MEMBER_NOT_FOUND\nSTUDY_NOT_FOUND\nMEMBER_STUDY_NOT_FOUND"),
    })
    public ResponseEntity<String> getMemberAuthority(@PathVariable("studyId") Long studyId) {
        return new ResponseEntity<>(studyService.getMemberAuthority(studyId), HttpStatus.OK);
    }

    @GetMapping("/member/{studyId}")
    @ApiOperation(value = "스터디 구성원 조회", notes = "<strong>받은 스터디 Id</strong>로 스터디 관리의 구성원 조회")
    @ApiResponses({
        @ApiResponse(code = 200, message = "스터디 구성원 리스트"),
        @ApiResponse(code = 404, message = "MEMBER_NOT_FOUND\nSTUDY_NOT_FOUND\nMEMBER_STUDY_NOT_FOUND"),
    })
    public ResponseEntity<List<StudyMemberResponseDto>> getMembersInStudy(@PathVariable("studyId") Long studyId) {
        return new ResponseEntity<>(studyService.getMembersInStudy(studyId), HttpStatus.OK);
    }

    @GetMapping("/cover-pic/{studyId}")
    @ApiOperation(value = "스터디 사진 정보", notes = "<strong>받은 스터디 id</strong>로 스터디 사진을 가져온다.")
    @ApiResponses({
        @ApiResponse(code = 200, message = "다운로드 uri"),
        @ApiResponse(code = 404, message = "STUDY_NOT_FOUND"),
    })
    public ResponseEntity<DBFileDto> getCoverPicUri(@PathVariable("studyId") Long studyId) {
        return new ResponseEntity<>(studyService.getCoverPicUri(studyId), HttpStatus.OK);
    }

}
