package com.mh.match.invite.controller;

import com.mh.match.invite.service.LinkService;
import com.mh.match.invite.dto.InviteLinkResponseDto;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/invite")
public class InviteController {

    private final LinkService linkService;

    @GetMapping("/{category}/{id}")
    @ApiOperation(value = "초대 링크 생성", notes = "<strong>받은 카테고리와 (클럽, 스터디, 프로젝트) id로</strong> 해당 상세조회 링크를 생성한다.")
    @ApiResponses({
        @ApiResponse(code = 200, message = "링크가 복사되었습니다."),
        @ApiResponse(code = 404, message = ""),
    })
    public ResponseEntity<InviteLinkResponseDto> makeInviteLink(@PathVariable("category") String category, @PathVariable("id") Long id) {
        return new ResponseEntity<>(linkService.makeInviteLink(category, id), HttpStatus.OK);
    }
}
