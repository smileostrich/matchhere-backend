package com.mh.match.group.projectboard.board.controller;


import com.mh.match.group.projectboard.board.service.ProjectBoardService;
import com.mh.match.group.projectboard.board.dto.ProjectBoardCreateRequestDto;
import com.mh.match.group.projectboard.board.dto.ProjectBoardInfoDto;
import com.mh.match.group.projectboard.board.dto.ProjectBoardUpdateDto;
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
@RequestMapping("/project")
public class ProjectBoardController {
    private final ProjectBoardService projectBoardService;

    @GetMapping("/{projectid}/boards")
    @ApiOperation(value = "(프로젝트)게시판 리스트 조회", notes = "<strong>받은 프로젝트 id</strong>를 사용해서 게시판들을 조회한다.")
    @ApiResponses({
        @ApiResponse(code = 200, message = "성공"),
        @ApiResponse(code = 404, message = "PROJECT_NOT_FOUND\nBOARD_NOT_FOUND\nDELETED_PROJECT"),
    })
    public ResponseEntity<List<ProjectBoardInfoDto>> getBoards(@PathVariable("projectid") Long projectid) {
        return new ResponseEntity<>(projectBoardService.getProjectBoards(projectid), HttpStatus.OK);
    }

    @PostMapping("/{projectid}/boards")
    @ApiOperation(value = "(프로젝트)게시판 생성", notes = "<strong>받은 프로젝트 id</strong>를 사용해서 게시판을 생성한다.")
    @ApiResponses({
        @ApiResponse(code = 200, message = "게시판 정보"),
        @ApiResponse(code = 404, message = "PROJECT_NOT_FOUND\nBOARD_NOT_FOUND\nDELETED_PROJECT"),
    })
    public ResponseEntity<ProjectBoardInfoDto> create(@PathVariable("projectid") Long projectid, @Valid @RequestBody ProjectBoardCreateRequestDto dto) {
        return new ResponseEntity<>(projectBoardService.createBoard(projectid, dto), HttpStatus.OK);
    }

    @DeleteMapping("/boards/{boardid}")
    @ApiOperation(value = "(프로젝트)게시판 삭제", notes = "<strong>받은 게시판 id</strong>를 사용해서 게시판을 삭제한다.")
    @ApiResponses({
        @ApiResponse(code = 200, message = "성공"),
        @ApiResponse(code = 404, message = "PROJECT_NOT_FOUND\nBOARD_NOT_FOUND\nDELETED_PROJECT"),
    })
    public ResponseEntity<String> deleteBoard(@PathVariable("boardid") Integer boardid){
        return new ResponseEntity<>("게시판이 삭제되었습니다.", projectBoardService.deleteBoard(boardid));
    }

    @PutMapping("/boards/{boardid}")
    @ApiOperation(value = "(프로젝트)게시판 수정", notes = "<strong>받은 게시판 id</strong>를 사용해서 게시판을 수정한다.")
    @ApiResponses({
        @ApiResponse(code = 200, message = "변경 후 정보"),
        @ApiResponse(code = 404, message = "PROJECT_NOT_FOUND\nBOARD_NOT_FOUND\nDELETED_PROJECT"),
    })
    public ResponseEntity<ProjectBoardInfoDto> updateBoard(@PathVariable("boardid") Integer boardid, @Valid @RequestBody ProjectBoardUpdateDto dto) {
        return new ResponseEntity<>(projectBoardService.updateBoard(boardid, dto), HttpStatus.OK);
    }
}
