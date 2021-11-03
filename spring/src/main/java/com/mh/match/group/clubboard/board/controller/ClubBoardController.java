package com.mh.match.group.clubboard.board.controller;


import com.mh.match.group.clubboard.board.dto.ClubBoardInfoDto;
import com.mh.match.group.clubboard.board.service.ClubBoardService;
import com.mh.match.group.clubboard.board.dto.ClubBoardCreateRequestDto;
import com.mh.match.group.clubboard.board.dto.ClubBoardUpdateDto;
import io.swagger.annotations.ApiOperation;
import java.util.List;

import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@RestController
@RequiredArgsConstructor
@RequestMapping("/club")
public class ClubBoardController {
    private final ClubBoardService clubBoardService;

    @GetMapping("/{clubId}/boards")
    @ApiOperation(value = "(클럽)게시판 리스트 조회", notes = "<strong>받은 클럽 id</strong>를 사용해서 게시판들을 조회한다.")
    @ApiResponses({
            @ApiResponse(code = 200, message = "성공"),
            @ApiResponse(code = 404, message = "CLUB_NOT_FOUND\nBOARD_NOT_FOUND\nDELETED_CLUB"),
    })
    public ResponseEntity<List<ClubBoardInfoDto>> getBoards(@PathVariable("clubId") Long clubId) {
        return new ResponseEntity<>(clubBoardService.getClubBoards(clubId), HttpStatus.OK);
    }

    @PostMapping("/{clubId}/boards")
    @ApiOperation(value = "(클럽)게시판 생성", notes = "<strong>받은 클럽 id</strong>를 사용해서 게시판을 생성한다.")
    @ApiResponses({
            @ApiResponse(code = 200, message = "게시판 정보"),
            @ApiResponse(code = 404, message = "CLUB_NOT_FOUND\nBOARD_NOT_FOUND\nDELETED_CLUB"),
    })
    public ResponseEntity<ClubBoardInfoDto> create(@PathVariable("clubId") Long clubId, @Valid @RequestBody ClubBoardCreateRequestDto dto) {
        return new ResponseEntity<>(clubBoardService.createBoard(clubId, dto), HttpStatus.OK);
    }

    @DeleteMapping("/boards/{boardId}")
    @ApiOperation(value = "(클럽)게시판 삭제", notes = "<strong>받은 게시판 id</strong>를 사용해서 게시판을 삭제한다.")
    @ApiResponses({
            @ApiResponse(code = 200, message = "성공"),
            @ApiResponse(code = 404, message = "CLUB_NOT_FOUND\nBOARD_NOT_FOUND\nDELETED_CLUB"),
    })
    public ResponseEntity<String> deleteBoard(@PathVariable("boardId") Integer boardId){
        return new ResponseEntity<>("게시판이 삭제되었습니다.", clubBoardService.deleteBoard(boardId));
    }

    @PutMapping("/boards/{boardId}")
    @ApiOperation(value = "(클럽)게시판 수정", notes = "<strong>받은 게시판 id</strong>를 사용해서 게시판을 수정한다.")
    @ApiResponses({
            @ApiResponse(code = 200, message = "변경 후 정보"),
            @ApiResponse(code = 404, message = "CLUB_NOT_FOUND\nBOARD_NOT_FOUND\nDELETED_CLUB"),
    })
    public ResponseEntity<ClubBoardInfoDto> updateBoard(@PathVariable("boardId") Integer boardId, @Valid @RequestBody ClubBoardUpdateDto dto) {
        return new ResponseEntity<>(clubBoardService.updateBoard(boardId, dto), HttpStatus.OK);
    }
}