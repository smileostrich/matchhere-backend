package com.mh.match.group.club.controller;

import com.mh.match.s3.dto.DBFileDto;
import com.mh.match.group.club.dto.request.ClubCreateRequestDto;
import com.mh.match.group.club.dto.request.ClubUpdateRequestDto;
import com.mh.match.group.club.dto.response.ClubInfoForUpdateResponseDto;
import com.mh.match.group.club.dto.response.ClubInfoResponseDto;
import com.mh.match.group.club.dto.response.ClubMemberResponseDto;
import com.mh.match.group.club.dto.response.ClubSimpleInfoResponseDto;
import com.mh.match.group.club.service.ClubService;
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
@RequestMapping("/club")
public class ClubController {

    private final ClubService clubService;

    @GetMapping("/infoforupdate/{clubId}")
    @ApiOperation(value = "클럽 업데이트를 위한 정보",
        notes = "<strong>받은 클럽 id</strong>로 해당 클럽 정보 + 수정을 위한 정보(사용자 클럽 리스트, 지역, 상태 리스트 등")
    @ApiResponses({
        @ApiResponse(code = 200, message = "정보 조회"),
        @ApiResponse(code = 401, message = "UNAUTHORIZED_CHANGE"),
        @ApiResponse(code = 404, message = "MEMBER_NOT_FOUND\nCLUB_NOT_FOUND"),
    })
    public ResponseEntity<ClubInfoForUpdateResponseDto> getInfoForUpdate(@PathVariable("clubId") Long clubId) {
        return ResponseEntity.ok(clubService.getInfoForUpdateClub(clubId));
    }

    @PostMapping(value = "", consumes = { MediaType.APPLICATION_JSON_VALUE, MediaType.MULTIPART_FORM_DATA_VALUE })
    @ApiOperation(value = "클럽 생성", notes = "<strong>받은 클럽 정보</strong>를 사용해서 클럽을 생성한다.")
    @ApiResponses({
        @ApiResponse(code = 200, message = "생성한 클럽 정보"),
        @ApiResponse(code = 400, message = "MEMBER_COUNT_OVER\nMEMBER_COUNT_BELOW_ZERO"),
        @ApiResponse(code = 404, message = "CITY_NOT_FOUND\nMEMBER_NOT_FOUND\nCLUB_NOT_FOUND\nFILE_NOT_FOUND\nAUTHORITY_NOT_FOUND"),
    })
    public ResponseEntity<ClubInfoResponseDto> create(@RequestPart(value="file", required=false) MultipartFile file, @RequestPart(value="key", required=true) ClubCreateRequestDto dto) {
        return ResponseEntity.ok(clubService.create(file, dto));
    }

    @PutMapping("/{clubId}")
    @ApiOperation(value = "클럽 수정", notes = "<strong>받은 클럽 정보</strong>를 사용해서 클럽를 수정한다.")
    @ApiResponses({
        @ApiResponse(code = 200, message = "수정된 정보"),
        @ApiResponse(code = 400, message = "MEMBER_COUNT_OVER"),
        @ApiResponse(code = 401, message = "UNAUTHORIZED_CHANGE"),
        @ApiResponse(code = 404, message = "CITY_NOT_FOUND\nMEMBER_NOT_FOUND\nCLUB_NOT_FOUND\nCLUB_NOT_FOUND\nMEMBER_CLUB_NOT_FOUND")
    })
    public ResponseEntity<ClubInfoResponseDto> update(@PathVariable("clubId") Long clubId,
        @Valid @RequestBody ClubUpdateRequestDto dto) {
        return ResponseEntity.ok(clubService.update(clubId, dto));
    }

    @PutMapping(value = "/cover-pic/{clubId}", consumes = { MediaType.MULTIPART_FORM_DATA_VALUE })
    @ApiOperation(value = "사진 변경", notes = "<strong>받은 클럽 id와 넣을 uuid을 받아</strong>로 사진을 변경시킨다.")
    @ApiResponses({
        @ApiResponse(code = 200, message = "성공"),
        @ApiResponse(code = 404, message = "CLUB_NOT_FOUND\nFILE_NOT_FOUND"),
    })
    public ResponseEntity<DBFileDto> changeCoverPic(@RequestPart(value="file", required=false) MultipartFile file, @PathVariable("clubId") Long clubId) {
        return new ResponseEntity<>(clubService.changeCoverPic(file, clubId), HttpStatus.OK);
    }

    @PutMapping("/view-count/{clubId}")
    @ApiOperation(value = "조회 수 증가", notes = "<strong>받은 클럽 id</strong>로 조회수를 증가시킨다.")
    @ApiResponses({
        @ApiResponse(code = 200, message = "처리되었습니다."),
        @ApiResponse(code = 404, message = "CLUB_NOT_FOUND"),
    })
    public ResponseEntity<String> plusViewCount(@PathVariable("clubId") Long clubId) {
        return new ResponseEntity<>("처리되었습니다.", clubService.plusViewCount(clubId));
    }

    @PutMapping("/authority/{clubId}/{memberId}/{authority}")
    @ApiOperation(value = "권한 변경", notes = "<strong>받은 클럽 id와 변경 시킬 멤버의 id와 변경하고자하는 authority을 받아</strong>로 권한을 변경시킨다.")
    @ApiResponses({
        @ApiResponse(code = 200, message = "권한이 변경되었습니다."),
        @ApiResponse(code = 401, message = "UNAUTHORIZED_CHANGE"),
        @ApiResponse(code = 404, message = "GROUP_AUTHORITY_NOT_FOUND\nMEMBER_NOT_FOUND\nCLUB_NOT_FOUND\nMEMBER_CLUB_NOT_FOUND\nROLE_NOT_FOUND"),
    })
    public ResponseEntity<String> changeAuthority(@PathVariable("clubId") Long clubId,
        @PathVariable("memberId") Long memberId, @PathVariable("authority") String authority) {
        return new ResponseEntity<>("권한이 변경되었습니다.", clubService.changeAuthority(clubId, memberId, authority));
    }

    @DeleteMapping("/{clubId}")
    @ApiOperation(value = "클럽 삭제", notes = "<strong>받은 클럽 Id</strong>로 클럽 관련 정보(멤버 관계, 사진, 주제)를 삭제한다.")
    @ApiResponses({
        @ApiResponse(code = 200, message = "삭제되었습니다."),
        @ApiResponse(code = 401, message = "UNAUTHORIZED_CHANGE"),
        @ApiResponse(code = 404, message = "MEMBER_NOT_FOUND\nCLUB_NOT_FOUND\nMEMBER_CLUB_NOT_FOUND\nFILE_NOT_FOUND"),
    })
    public ResponseEntity<HttpStatus> delete(@PathVariable("clubId") Long clubId) {
        return ResponseEntity.ok(clubService.delete(clubId));
    }

    @DeleteMapping("/{clubId}/member")
    @ApiOperation(value = "클럽 탈퇴", notes = "<strong>받은 클럽 id</strong>로 클럽에서 탈퇴한다.")
    @ApiResponses({
        @ApiResponse(code = 200, message = "탈퇴되었습니다."),
        @ApiResponse(code = 400, message = "MEMBER_COUNT_BELOW_ZERO\nHOST_CANNOT_LEAVE"),
        @ApiResponse(code = 404, message = "MEMBER_NOT_FOUND\nCLUB_NOT_FOUND\nMEMBER_CLUB_NOT_FOUND"),
    })
    public ResponseEntity<String> removeMe(@PathVariable("clubId") Long clubId) {
        return new ResponseEntity<>("탈퇴되었습니다.", clubService.removeMe(clubId));
    }

    @DeleteMapping("/{clubId}/{memberId}")
    @ApiOperation(value = "클럽 추방", notes = "<strong>받은 클럽 id와 탈퇴시킬 멤버의 id</strong>로 클럽에서 탈퇴시킨다.")
    @ApiResponses({
        @ApiResponse(code = 200, message = "처리되었습니다."),
        @ApiResponse(code = 400, message = "MEMBER_COUNT_BELOW_ZERO\nONLY_CAN_REMOVE_COMMON\nCOMMON_MEMBER_CANNOT_REMOVE"),
        @ApiResponse(code = 404, message = "MEMBER_NOT_FOUND\nCLUB_NOT_FOUND\nMEMBER_CLUB_NOT_FOUND"),
    })
    public ResponseEntity<String> removeMember(@PathVariable("clubId") Long clubId,
        @PathVariable("memberId") Long memberId) {
        return new ResponseEntity<>("처리되었습니다.", clubService.removeMember(clubId, memberId));
    }

    @GetMapping
    @ApiOperation(value = "모든 클럽 조회", notes = "클럽 종료가 아닌 // 모집 중 // 전체 공개 // 를 만족하는 클럽들을 작성일 기준 내림차순으로 받는다")
    @ApiResponses({
        @ApiResponse(code = 200, message = "페이징된 클럽 조회"),
    })
    public ResponseEntity<Page<ClubSimpleInfoResponseDto>> getAllClub(
        @PageableDefault(size = 10) @SortDefault(sort = "createdDate", direction = Sort.Direction.DESC) Pageable pageable) {
        return ResponseEntity.ok(clubService.getAllClub(pageable));
    }

//    @GetMapping("/recommend")
//    @ApiOperation(value = "모든 클럽 조회", notes = "추천 하는 클럽들을 리턴한다")
//    public ResponseEntity<Page<ClubInfoResponseDto>> getAllClubWithRecommend(@SortDefault.SortDefaults({
////            @SortDefault(sort = "createDate", direction= Sort.Direction.DESC),
//            @SortDefault(sort = "period", direction = Sort.Direction.DESC),
//            @SortDefault(sort = "maxCount", direction = Sort.Direction.DESC)
//    }) @PageableDefault(size = 10) Pageable pageable) throws Exception {
//        return ResponseEntity.ok(clubService.getAllClub(pageable));
//    }

    @GetMapping("/{clubId}")
    @ApiOperation(value = "클럽 상세정보 조회",
        notes = "<strong>받은 클럽 id</strong>로 해당 클럽 정보 + 수정을 위한 정보(사용자 클럽 리스트, 지역, 상태 리스트 등")
    @ApiResponses({
        @ApiResponse(code = 200, message = "클럽 상세 정보"),
        @ApiResponse(code = 404, message = "CLUB_NOT_FOUND\nDELETED_CLUB"),
    })
    public ResponseEntity<ClubInfoResponseDto> getOneClub(@PathVariable("clubId") Long clubId) {
        return ResponseEntity.ok(clubService.getOneClub(clubId));
    }

    @GetMapping("/one/simple/{clubId}")
    @ApiOperation(value = "클럽 간편정보 조회", notes = "<strong>받은 클럽 id</strong>로 클럽 관리 화면의 간편 정보 조회")
    @ApiResponses({
        @ApiResponse(code = 200, message = "클럽 간편 정보"),
        @ApiResponse(code = 404, message = "CLUB_NOT_FOUND\nDELETED_CLUB"),
    })
    public ResponseEntity<ClubSimpleInfoResponseDto> getOneSimpleClub(@PathVariable("clubId") Long clubId) {
        return ResponseEntity.ok(clubService.getOneSimpleClub(clubId));
    }

    @GetMapping("/authority/{clubId}")
    @ApiOperation(value = "현 사용자의 권한 정보", notes = "<strong>받은 클럽 id</strong>로 현 사용자에 대한 권한을 확인한다.")
    @ApiResponses({
        @ApiResponse(code = 200, message = "소유자, 관리자, 게스트, 방문객"),
        @ApiResponse(code = 404, message = "MEMBER_NOT_FOUND\nCLUB_NOT_FOUND\nMEMBER_CLUB_NOT_FOUND"),
    })
    public ResponseEntity<String> getMemberAuthority(@PathVariable("clubId") Long clubId) {
        return new ResponseEntity<>(clubService.getMemberAuthority(clubId), HttpStatus.OK);
    }

    @GetMapping("/member/{clubId}")
    @ApiOperation(value = "클럽 구성원 조회", notes = "<strong>받은 클럽 Id</strong>로 클럽 관리의 구성원 조회")
    @ApiResponses({
        @ApiResponse(code = 200, message = "클럽 구성원 리스트"),
        @ApiResponse(code = 404, message = "MEMBER_NOT_FOUND\nCLUB_NOT_FOUND\nMEMBER_CLUB_NOT_FOUND"),
    })
    public ResponseEntity<List<ClubMemberResponseDto>> getMembersInClub(@PathVariable("clubId") Long clubId) {
        return new ResponseEntity<>(clubService.getMembersInClub(clubId), HttpStatus.OK);
    }

    @GetMapping("/cover-pic/{clubId}")
    @ApiOperation(value = "클럽 사진 정보", notes = "<strong>받은 클럽 id</strong>로 클럽 사진을 가져온다.")
    @ApiResponses({
        @ApiResponse(code = 200, message = "다운로드 uri"),
        @ApiResponse(code = 404, message = "CLUB_NOT_FOUND"),
    })
    public ResponseEntity<DBFileDto> getCoverPicUri(@PathVariable("clubId") Long clubId) {
        return new ResponseEntity<>(clubService.getCoverPicUri(clubId), HttpStatus.OK);
    }

}
