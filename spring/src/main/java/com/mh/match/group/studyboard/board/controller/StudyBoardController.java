package com.mh.match.group.studyboard.board.controller;


import com.mh.match.group.studyboard.board.service.StudyBoardService;
import com.mh.match.group.studyboard.board.dto.StudyBoardCreateRequestDto;
import com.mh.match.group.studyboard.board.dto.StudyBoardInfoDto;
import com.mh.match.group.studyboard.board.dto.StudyBoardUpdateDto;
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
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/study")
public class StudyBoardController {

    private final StudyBoardService studyBoardService;

    @GetMapping("/{studyId}/boards")
    @ApiOperation(value = "(스터디)게시판 리스트 조회", notes = "<strong>받은 스터디 id</strong>를 사용해서 게시판들을 조회한다.")
    @ApiResponses({
        @ApiResponse(code = 200, message = "성공"),
        @ApiResponse(code = 404, message = "STUDY_NOT_FOUND\nBOARD_NOT_FOUND\nDELETED_STUDY"),
    })
    public ResponseEntity<List<StudyBoardInfoDto>> getBoards(@PathVariable("studyId") Long studyId) {
        return new ResponseEntity<>(studyBoardService.getStudyBoards(studyId), HttpStatus.OK);
    }

    @PostMapping("/{studyId}/boards")
    @ApiOperation(value = "(스터디)게시판 생성", notes = "<strong>받은 스터디 id</strong>를 사용해서 게시판을 생성한다.")
    @ApiResponses({
        @ApiResponse(code = 200, message = "게시판 정보"),
        @ApiResponse(code = 404, message = "STUDY_NOT_FOUND\nBOARD_NOT_FOUND\nDELETED_STUDY"),
    })
    public ResponseEntity<StudyBoardInfoDto> create(@PathVariable("studyId") Long studyId, @Valid @RequestBody StudyBoardCreateRequestDto dto) {
        return new ResponseEntity<>(studyBoardService.createBoard(studyId, dto), HttpStatus.OK);
    }

    @DeleteMapping("/boards/{boardId}")
    @ApiOperation(value = "(스터디)게시판 삭제", notes = "<strong>받은 게시판 id</strong>를 사용해서 게시판을 삭제한다.")
    @ApiResponses({
        @ApiResponse(code = 200, message = "성공"),
        @ApiResponse(code = 404, message = "STUDY_NOT_FOUND\nBOARD_NOT_FOUND\nDELETED_STUDY"),
    })
    public ResponseEntity<String> deleteBoard(@PathVariable("boardId") Integer boardId){
        return new ResponseEntity<>("게시판이 삭제되었습니다.", studyBoardService.deleteBoard(boardId));
    }

    @PutMapping("/boards/{boardId}")
    @ApiOperation(value = "(스터디)게시판 수정", notes = "<strong>받은 게시판 id</strong>를 사용해서 게시판을 수정한다.")
    @ApiResponses({
        @ApiResponse(code = 200, message = "변경 후 정보"),
        @ApiResponse(code = 404, message = "STUDY_NOT_FOUND\nBOARD_NOT_FOUND\nDELETED_STUDY"),
    })
    public ResponseEntity<StudyBoardInfoDto> updateBoard(@PathVariable("boardId") Integer boardId, @Valid @RequestBody StudyBoardUpdateDto dto) {
        return new ResponseEntity<>(studyBoardService.updateBoard(boardId, dto), HttpStatus.OK);
    }
}
