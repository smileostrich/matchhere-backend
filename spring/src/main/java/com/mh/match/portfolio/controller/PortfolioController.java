package com.mh.match.portfolio.controller;

import com.mh.match.portfolio.dto.request.PortfolioCreateRequestDto;
import com.mh.match.portfolio.dto.request.PortfolioUpdateRequestDto;
import com.mh.match.portfolio.dto.response.PortfolioInfoForUpdateResponseDto;
import com.mh.match.portfolio.dto.response.PortfolioInfoResponseDto;
import com.mh.match.portfolio.entity.Portfolio;
import com.mh.match.portfolio.service.PortfolioService;
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
@RequestMapping("/portfolio")
public class PortfolioController {

    private final PortfolioService portfolioService;

    @GetMapping("/infoforupdate/{portfolioId}")
    @ApiOperation(value = "클럽 업데이트를 위한 정보", notes = "<strong>받은 클럽 id</strong>로 해당 클럽 정보 + 수정을 위한 정보(사용자 클럽 리스트, 지역, 상태 리스트 등")
    @ApiResponses({
        @ApiResponse(code = 200, message = "정보 조회"),
        @ApiResponse(code = 401, message = "UNAUTHORIZED_CHANGE"),
        @ApiResponse(code = 404, message = "MEMBER_NOT_FOUND\nCLUB_NOT_FOUND"),
    })
    public ResponseEntity<PortfolioInfoForUpdateResponseDto> getInfoForUpdate(@PathVariable("portfolioId") Long portfolioId) {
        return ResponseEntity.ok(portfolioService.getInfoForUpdatePortfolio(portfolioId));
    }

    @PostMapping(value = "", consumes = { MediaType.APPLICATION_JSON_VALUE, MediaType.MULTIPART_FORM_DATA_VALUE })
    @ApiOperation(value = "클럽 생성", notes = "<strong>받은 클럽 정보</strong>를 사용해서 클럽을 생성한다.")
    @ApiResponses({
        @ApiResponse(code = 200, message = "생성한 클럽 정보"),
        @ApiResponse(code = 400, message = "MEMBER_COUNT_OVER\nMEMBER_COUNT_BELOW_ZERO"),
        @ApiResponse(code = 404, message = "CITY_NOT_FOUND\nMEMBER_NOT_FOUND\nCLUB_NOT_FOUND\nFILE_NOT_FOUND\nAUTHORITY_NOT_FOUND"),
    })
    public ResponseEntity<PortfolioInfoResponseDto> create(@RequestPart(value="file", required=false) MultipartFile file, @RequestPart(value="key", required=true) PortfolioCreateRequestDto dto) {
        return ResponseEntity.ok(portfolioService.create(file, dto));
    }

    @PutMapping("/{portfolioId}")
    @ApiOperation(value = "클럽 수정", notes = "<strong>받은 클럽 정보</strong>를 사용해서 클럽를 수정한다.")
    @ApiResponses({
        @ApiResponse(code = 200, message = "수정된 정보"),
        @ApiResponse(code = 400, message = "MEMBER_COUNT_OVER"),
        @ApiResponse(code = 401, message = "UNAUTHORIZED_CHANGE"),
        @ApiResponse(code = 404, message = "CITY_NOT_FOUND\nMEMBER_NOT_FOUND\nCLUB_NOT_FOUND\nCLUB_NOT_FOUND\nMEMBER_CLUB_NOT_FOUND")
    })
    public ResponseEntity<PortfolioInfoResponseDto> update(@PathVariable("portfolioId") Long portfolioId,
        @Valid @RequestBody PortfolioUpdateRequestDto dto) {
        return ResponseEntity.ok(portfolioService.update(portfolioId, dto));
    }

    @PutMapping(value = "/cover-pic/{portfolioId}", consumes = { MediaType.MULTIPART_FORM_DATA_VALUE })
    @ApiOperation(value = "사진 변경", notes = "<strong>받은 클럽 id와 넣을 uuid을 받아</strong>로 사진을 변경시킨다.")
    @ApiResponses({
        @ApiResponse(code = 200, message = "성공"),
        @ApiResponse(code = 404, message = "CLUB_NOT_FOUND\nFILE_NOT_FOUND"),
    })
    public ResponseEntity<DBFileDto> changeCoverPic(@RequestPart(value="file", required=false) MultipartFile file, @PathVariable("portfolioId") Long portfolioId) {
        return new ResponseEntity<>(portfolioService.changeCoverPic(file, portfolioId), HttpStatus.OK);
    }

    @PutMapping("/view-count/{portfolioId}")
    @ApiOperation(value = "조회 수 증가", notes = "<strong>받은 클럽 id</strong>로 조회수를 증가시킨다.")
    @ApiResponses({
        @ApiResponse(code = 200, message = "처리되었습니다."),
        @ApiResponse(code = 404, message = "CLUB_NOT_FOUND"),
    })
    public ResponseEntity<String> plusViewCount(@PathVariable("portfolioId") Long portfolioId) {
        return new ResponseEntity<>("처리되었습니다.", portfolioService.plusViewCount(portfolioId));
    }

    @DeleteMapping("/{portfolioId}")
    @ApiOperation(value = "클럽 삭제", notes = "<strong>받은 클럽 Id</strong>로 클럽 관련 정보(멤버 관계, 사진, 주제)를 삭제한다.")
    @ApiResponses({
        @ApiResponse(code = 200, message = "삭제되었습니다."),
        @ApiResponse(code = 401, message = "UNAUTHORIZED_CHANGE"),
        @ApiResponse(code = 404, message = "MEMBER_NOT_FOUND\nCLUB_NOT_FOUND\nMEMBER_CLUB_NOT_FOUND\nFILE_NOT_FOUND"),
    })
    public ResponseEntity<HttpStatus> delete(@PathVariable("portfolioId") Long portfolioId) {
        return ResponseEntity.ok(portfolioService.delete(portfolioId));
    }

//    @GetMapping
//    @ApiOperation(value = "모든 클럽 조회", notes = "클럽 종료가 아닌 // 모집 중 // 전체 공개 // 를 만족하는 클럽들을 작성일 기준 내림차순으로 받는다")
//    @ApiResponses({
//        @ApiResponse(code = 200, message = "페이징된 클럽 조회"),
//    })
//    public ResponseEntity<Page<ClubSimpleInfoResponseDto>> getAllClub(
//        @PageableDefault(size = 10) @SortDefault(sort = "createdDate", direction = Sort.Direction.DESC) Pageable pageable) {
//        return ResponseEntity.ok(clubService.getAllClub(pageable));
//    }

    @GetMapping("/{clubId}")
    @ApiOperation(value = "클럽 상세정보 조회",
        notes = "<strong>받은 클럽 id</strong>로 해당 클럽 정보 + 수정을 위한 정보(사용자 클럽 리스트, 지역, 상태 리스트 등")
    @ApiResponses({
        @ApiResponse(code = 200, message = "클럽 상세 정보"),
        @ApiResponse(code = 404, message = "CLUB_NOT_FOUND\nDELETED_CLUB"),
    })
    public ResponseEntity<PortfolioInfoResponseDto> getOneClub(@PathVariable("portfolioId") Long portfolioId) {
        return ResponseEntity.ok(portfolioService.getOnePortfolio(portfolioId));
    }

//    @GetMapping("/one/simple/{clubId}")
//    @ApiOperation(value = "클럽 간편정보 조회", notes = "<strong>받은 클럽 id</strong>로 클럽 관리 화면의 간편 정보 조회")
//    @ApiResponses({
//        @ApiResponse(code = 200, message = "클럽 간편 정보"),
//        @ApiResponse(code = 404, message = "CLUB_NOT_FOUND\nDELETED_CLUB"),
//    })
//    public ResponseEntity<ClubSimpleInfoResponseDto> getOneSimpleClub(@PathVariable("clubId") Long clubId) {
//        return ResponseEntity.ok(clubService.getOneSimpleClub(clubId));
//    }

//    @GetMapping("/cover-pic/{clubId}")
//    @ApiOperation(value = "클럽 사진 정보", notes = "<strong>받은 클럽 id</strong>로 클럽 사진을 가져온다.")
//    @ApiResponses({
//        @ApiResponse(code = 200, message = "다운로드 uri"),
//        @ApiResponse(code = 404, message = "CLUB_NOT_FOUND"),
//    })
//    public ResponseEntity<DBFileDto> getCoverPicUri(@PathVariable("clubId") Long clubId) {
//        return new ResponseEntity<>(clubService.getCoverPicUri(clubId), HttpStatus.OK);
//    }

}
