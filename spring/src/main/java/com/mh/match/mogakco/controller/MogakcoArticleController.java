package com.mh.match.mogakco.controller;


import com.mh.match.group.clubboard.article.dto.ClubArticleInfoResponseDto;
import com.mh.match.group.clubboard.article.dto.ClubArticleRequestDto;
import com.mh.match.group.clubboard.article.dto.ClubArticleSimpleInfoResponseDto;
import com.mh.match.group.clubboard.article.service.ClubArticleService;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.data.web.SortDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequiredArgsConstructor
@RequestMapping("/mogakco")
public class MogakcoArticleController {
    private final ClubArticleService clubArticleService;

    @GetMapping
    @ApiOperation(value = "(모각코)게시글 리스트 조회", notes = "<strong>받은 게시판 id</strong>를 사용해서 게시글들을 조회한다.")
    @ApiResponses({
            @ApiResponse(code = 200, message = "게시글 리스트 조회"),
            @ApiResponse(code = 404, message = "BOARD_NOT_FOUND\nMEMBER_NOT_FOUND\nCONTENT_NOT_FOUND\nARTICLE_NOT_FOUND"),
    })
    public ResponseEntity<Page<ClubArticleSimpleInfoResponseDto>> getArticles(@PageableDefault(size = 10) @SortDefault(sort = "createDate", direction = Sort.Direction.DESC) Pageable pageable) {
        return new ResponseEntity<>(clubArticleService.getClubArticles(pageable), HttpStatus.OK);
    }

//    @GetMapping("/{articleId}")
//    @ApiOperation(value = "(모각코)게시글 상세조회", notes = "<strong>받은 article id</strong>를 사용해서 게시글 상세 조회")
//    @ApiResponses({
//            @ApiResponse(code = 200, message = "게시글 상제 정보"),
//            @ApiResponse(code = 404, message = "BOARD_NOT_FOUND\nMEMBER_NOT_FOUND\nCONTENT_NOT_FOUND\nARTICLE_NOT_FOUND"),
//    })
//    public ResponseEntity<ClubArticleInfoResponseDto> getArticleDetail(@PathVariable("articleId") Long articleId) {
//        return new ResponseEntity<>(clubArticleService.getClubArticleDetail(articleId), HttpStatus.OK);
//    }
//
//    @PostMapping
//    @ApiOperation(value = "(모각코) 게시글 생성", notes = "<strong>받은 게시판 id</strong>를 사용해서 게시글을 생성한다.")
//    @ApiResponses({
//            @ApiResponse(code = 200, message = "생성 게시글 정보"),
//            @ApiResponse(code = 404, message = "BOARD_NOT_FOUND\nMEMBER_NOT_FOUND\nCONTENT_NOT_FOUND"),
//    })
//    public ResponseEntity<ClubArticleInfoResponseDto> createArticle(@Valid @RequestBody ClubArticleRequestDto dto) {
//        return new ResponseEntity<>(clubArticleService.createArticle(dto), HttpStatus.OK);
//    }
//
//    @PutMapping("/{articleId}")
//    @ApiOperation(value = "(모각코) 게시글 수정", notes = "<strong>받은 게시글 id</strong>를 사용해서 게시글을 수정한다.")
//    @ApiResponses({
//            @ApiResponse(code = 200, message = "수정 후 정보"),
//            @ApiResponse(code = 404, message = "BOARD_NOT_FOUND\nMEMBER_NOT_FOUND\nCONTENT_NOT_FOUND\nARTICLE_NOT_FOUND"),
//    })
//    public ResponseEntity<ClubArticleInfoResponseDto> updateArticle(@PathVariable("articleId") Long articleId, @Valid @RequestBody ClubArticleRequestDto dto) {
//        return new ResponseEntity<>(clubArticleService.updateArticle(articleId, dto), HttpStatus.OK);
//    }
//
//    @DeleteMapping("/{articleId}")
//    @ApiOperation(value = "(모각코) 게시글 삭제", notes = "<strong>받은 게시글 id</strong>를 사용해서 게시글을 삭제한다.")
//    @ApiResponses({
//            @ApiResponse(code = 200, message = "수정되었습니다."),
//            @ApiResponse(code = 404, message = "BOARD_NOT_FOUND\nMEMBER_NOT_FOUND\nCONTENT_NOT_FOUND\nARTICLE_NOT_FOUND"),
//    })
//    public ResponseEntity<String> deleteArticle(@PathVariable("articleId") Long articleId) {
//        return new ResponseEntity<>("삭제되었습니다.", clubArticleService.deleteArticle(articleId));
//    }
}