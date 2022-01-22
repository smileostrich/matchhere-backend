package com.mh.match.member.controller;

import java.util.List;
import javax.validation.Valid;
import javax.validation.constraints.Positive;

import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import com.mh.match.member.dto.ChangePasswordDto;
import com.mh.match.member.dto.request.MemberAlarmRequestDto;
import com.mh.match.member.dto.request.MemberBasicInfoRequestDto;
import com.mh.match.member.dto.request.MemberCareerRequestDto;
import com.mh.match.member.dto.request.MemberCareerUpdateRequestDto;
import com.mh.match.member.dto.request.MemberCertificationRequestDto;
import com.mh.match.member.dto.request.MemberCertificationUpdateRequestDto;
import com.mh.match.member.dto.request.MemberCheckPasswordDto;
import com.mh.match.member.dto.request.MemberEducationRequestDto;
import com.mh.match.member.dto.request.MemberEducationUpdateRequestDto;
import com.mh.match.member.dto.request.MemberPortfolioRequestDto;
import com.mh.match.member.dto.request.MemberSkillRequestDto;
import com.mh.match.member.dto.request.MemberSnsRequestDto;
import com.mh.match.member.dto.response.CareerResponseDto;
import com.mh.match.member.dto.response.CertificationResponseDto;
import com.mh.match.member.dto.response.EducationResponseDto;
import com.mh.match.member.dto.response.MemberAlarmResponseDto;
import com.mh.match.member.dto.response.MemberBasicinfoResponseDto;
import com.mh.match.member.dto.response.MemberCareerAllResponseDto;
import com.mh.match.member.dto.response.MemberGroupResponseDto;
import com.mh.match.member.dto.response.MemberMeResponseDto;
import com.mh.match.member.dto.response.MemberSkillResponseDto;
import com.mh.match.member.dto.response.MemberSnsPortfolioResponseDto;
import com.mh.match.member.dto.response.MemberSnsResponseDto;
import com.mh.match.member.dto.response.MypageResponseDto;
import com.mh.match.member.dto.response.PortfolioResponseDto;
import com.mh.match.member.service.MemberService;
import com.mh.match.s3.dto.DBFileDto;
import com.mh.match.s3.service.S3Service;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@RestController
@RequestMapping("/member")
public class MemberController {
    private final MemberService memberService;
    private final S3Service service;

    @GetMapping("/me")
    @ApiOperation(value = "내 정보")
    public ResponseEntity<MemberMeResponseDto> getMe() {
        return ResponseEntity.ok(memberService.getMe());
    }

    @PostMapping("/check/password")
    @ApiOperation(value = "비밀번호 체크")
    @ApiResponses({
            @ApiResponse(code = 200, message = "성공"),
    })
    public ResponseEntity<Boolean> checkPassword(@RequestBody MemberCheckPasswordDto memberCheckPasswordDto) {
        return ResponseEntity.ok(memberService.checkPassword(memberCheckPasswordDto));
    }

    @PutMapping(value="/coverpic", consumes = { MediaType.MULTIPART_FORM_DATA_VALUE })
    @ApiOperation(value = "프로필 이미지 변경")
    @ApiResponses({
            @ApiResponse(code = 200, message = "성공"),
    })
    public ResponseEntity<DBFileDto> updateCoverPic(@RequestPart(value="file", required=false) MultipartFile file) {
        return ResponseEntity.ok(memberService.updateCoverPic(file));
    }

    @DeleteMapping("/coverpic")
    @ApiOperation(value = "프로필 이미지 제거")
    public ResponseEntity<HttpStatus> deleteMemberCoverPic() {
        return ResponseEntity.ok(memberService.deleteMemberCoverPic());
    }

    @PutMapping("/password")
    @ApiOperation(value = "비밀번호 변경")
    @ApiResponses({
            @ApiResponse(code = 200, message = "성공"),
    })
    public ResponseEntity<HttpStatus> updatePassword(@RequestBody @Valid ChangePasswordDto changePasswordDto) {
        return ResponseEntity.ok(memberService.updatePassword(changePasswordDto));
    }

    @GetMapping("/mypage/{email}")
    @ApiOperation(value = "다른사람의 마이페이지")
    public ResponseEntity<MypageResponseDto> getMyPage(@PathVariable("email") String email) {
        return ResponseEntity.ok(memberService.getMyPage(email));
    }

    @GetMapping("/mypage")
    @ApiOperation(value = "마이 페이지")
    public ResponseEntity<MypageResponseDto> getMyPage() {
        return ResponseEntity.ok(memberService.getMyPage());
    }

    @GetMapping("/basicinfo")
    @ApiOperation(value = "내 기본정보 Get")
    public ResponseEntity<MemberBasicinfoResponseDto> getMemberBasicinfo() {
        return ResponseEntity.ok(memberService.getMemberBasicinfo());
    }

    @PutMapping("/basicinfo")
    @ApiOperation(value = "내 기본정보 Update")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "성공")
    })
    public ResponseEntity<HttpStatus> updateMemberBasicinfo(@RequestBody @Valid MemberBasicInfoRequestDto memberBasicinfoRequestDto) throws Exception {
        return ResponseEntity.ok(memberService.updateMemberBasicInfo(memberBasicinfoRequestDto));
    }

    @GetMapping("/skills")
    @ApiOperation(value = "내 직무/기술스택 Get")
    public ResponseEntity<MemberSkillResponseDto> getMemberSkills() {
        return ResponseEntity.ok(memberService.getMemberSkills());
    }

    @PutMapping("/skills")
    @ApiOperation(value = "내 직무/기술스택 Update")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "성공")
    })
    public ResponseEntity<HttpStatus> updateMemberSkills(@RequestBody @Valid MemberSkillRequestDto memberSkillRequestDto) throws Exception {
        return ResponseEntity.ok(memberService.updateMemberSkills(memberSkillRequestDto));
    }

    @GetMapping("/careerall")
    @ApiOperation(value = "내 커리어(경력,자격증,교육) Get")
    public ResponseEntity<MemberCareerAllResponseDto> getMemberCareerList() {
        return ResponseEntity.ok(memberService.getMemberCareerAll());
    }

    @GetMapping("/career/{id}")
    @ApiOperation(value = "id를 기반으로 해당 경력 Get")
    public ResponseEntity<CareerResponseDto> getMemberCareer(@PathVariable("id") Long id) {
        return ResponseEntity.ok(memberService.getMemberCareer(id));
    }

    @PostMapping("/career")
    @ApiOperation(value = "내 경력 Create")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "성공")
    })
    public ResponseEntity<CareerResponseDto> createMemberCareer(@RequestBody @Valid MemberCareerRequestDto memberCareerRequestDto) throws Exception {
        return ResponseEntity.ok(memberService.createMemberCareer(memberCareerRequestDto));
    }

    @PutMapping("/career/{id}")
    @ApiOperation(value = "내 경력 Update")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "성공")
    })
    public ResponseEntity<CareerResponseDto> updateMemberCareer(@PathVariable("id") Long id, @RequestBody @Valid MemberCareerUpdateRequestDto memberCareerUpdateRequestDto) {
        return ResponseEntity.ok(memberService.updateMemberCareer(id, memberCareerUpdateRequestDto));
    }

    @DeleteMapping("/career/{id}")
    @ApiOperation(value = "id를 기반으로 해당 경력 Delete")
    public ResponseEntity<HttpStatus> deleteMemberCareer(@PathVariable("id") Long id) {
        return ResponseEntity.ok(memberService.deleteMemberCareer(id));
    }

    @GetMapping("/certification/{id}")
    @ApiOperation(value = "id를 기반으로 해당 자격증 Get")
    public ResponseEntity<CertificationResponseDto> getMemberCertification(@PathVariable("id") Long id) {
        return ResponseEntity.ok(memberService.getMemberCertification(id));
    }

    @DeleteMapping("/certification/{id}")
    @ApiOperation(value = "id를 기반으로 해당 자격증 Delete")
    public ResponseEntity<HttpStatus> deleteMemberCertification(@PathVariable("id") Long id) {
        return ResponseEntity.ok(memberService.deleteMemberCertification(id));
    }

    @PostMapping("/certification")
    @ApiOperation(value = "내 자격증 Create")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "성공")
    })
    public ResponseEntity<CertificationResponseDto> createMemberCertification(@RequestBody @Valid MemberCertificationRequestDto memberCertificationRequestDto) throws Exception {
        return ResponseEntity.ok(memberService.createMemberCertification(memberCertificationRequestDto));
    }

    @PutMapping("/certification/{id}")
    @ApiOperation(value = "내 자격증 Update")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "성공")
    })
    public ResponseEntity<CertificationResponseDto> updateMemberCertification(@PathVariable("id") Long id, @RequestBody @Valid MemberCertificationUpdateRequestDto memberCertificationUpdateRequestDto) {
        return ResponseEntity.ok(memberService.updateMemberCertification(id, memberCertificationUpdateRequestDto));
    }

    @GetMapping("/education/{id}")
    @ApiOperation(value = "id를 기반으로 해당 교육 Get")
    public ResponseEntity<EducationResponseDto> getMemberEducation(@PathVariable("id") Long id) {
        return ResponseEntity.ok(memberService.getMemberEducation(id));
    }

    @DeleteMapping("/education/{id}")
    @ApiOperation(value = "id를 기반으로 해당 교육 Delete")
    public ResponseEntity<HttpStatus> deleteMemberEducation(@PathVariable("id") Long id) {
        return ResponseEntity.ok(memberService.deleteMemberEducation(id));
    }

    @PostMapping("/education")
    @ApiOperation(value = "내 교육 Create")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "성공")
    })
    public ResponseEntity<EducationResponseDto> createMemberCertification(@RequestBody @Valid MemberEducationRequestDto memberEducationRequestDto) throws Exception {
        return ResponseEntity.ok(memberService.createMemberEducation(memberEducationRequestDto));
    }

    @PutMapping("/education/{id}")
    @ApiOperation(value = "내 교육 Update")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "성공")
    })
    public ResponseEntity<EducationResponseDto> updateMemberEducation(@PathVariable("id") Long id, @RequestBody @Valid MemberEducationUpdateRequestDto memberEducationUpdateRequestDto) {
        return ResponseEntity.ok(memberService.updateMemberEducation(id, memberEducationUpdateRequestDto));
    }

    @GetMapping("/snsportfolio")
    @ApiOperation(value = "내 포트폴리오 Get")
    public ResponseEntity<MemberSnsPortfolioResponseDto> getMemberSnsPortfolio() {
        return ResponseEntity.ok(memberService.getMemberSnsPortfolio());
    }

//    @PostMapping("/portfolio")
//    @ApiOperation(value = "내 포트폴리오 Create")
//    @ApiResponses(value = {
//            @ApiResponse(code = 200, message = "성공")
//    })
//    public ResponseEntity<HttpStatus> createMemberPortfolio(@RequestBody @Valid MemberPortfolioRequestDto memberPortfolioRequestDto) throws Exception {
//        return ResponseEntity.ok(memberService.createMemberPortfolio(memberPortfolioRequestDto));
//    }

    @GetMapping("/portfolio")
    @ApiOperation(value = "내 포트폴리오 Get")
    public ResponseEntity<PortfolioResponseDto> getMemberPortfolio() {
        return ResponseEntity.ok(memberService.getMemberPortfolio());
    }

    @PutMapping(value="/portfolio", consumes = { MediaType.APPLICATION_JSON_VALUE, MediaType.MULTIPART_FORM_DATA_VALUE })
    @ApiOperation(value = "내 포트폴리오 Update")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "성공")
    })
    public ResponseEntity<PortfolioResponseDto> updateMemberPortfolio(@RequestPart(value="file", required=false) MultipartFile file, @RequestPart(value="key", required=false) MemberPortfolioRequestDto memberPortfolioRequestDto) throws Exception {
        return ResponseEntity.ok(memberService.updateMemberPortfolio(file, memberPortfolioRequestDto));
    }

    @DeleteMapping("/portfolio")
    @ApiOperation(value = "프로필 이미지 제거")
    public ResponseEntity<HttpStatus> deleteMemberPortfolio() {
        return ResponseEntity.ok(memberService.deleteMemberPortfolio());
    }


    @GetMapping("/sns")
    @ApiOperation(value = "내 sns Get")
    public ResponseEntity<MemberSnsResponseDto> getMemberSns() {
        return ResponseEntity.ok(memberService.getMemberSns());
    }

    @PutMapping("/sns")
    @ApiOperation(value = "내 SNS Update")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "성공")
    })
    public ResponseEntity<MemberSnsResponseDto> updateMemberSns(@RequestBody @Valid MemberSnsRequestDto memberSnsRequestDto) throws Exception {
        return ResponseEntity.ok(memberService.updateMemberSns(memberSnsRequestDto));
    }

    @DeleteMapping
    @ApiOperation(value = "회원 탈퇴")
    public ResponseEntity<?> deleteMember() {
        memberService.deleteMember();
        return new ResponseEntity<String>("회원탈퇴가 정상적으로 이루어졌습니다.", HttpStatus.OK);
    }

    @GetMapping("/group")
    @ApiOperation(value = "내 sns Get")
    public ResponseEntity<MemberGroupResponseDto> getMemberGroup() {
        return ResponseEntity.ok(memberService.getMemberGroup());
    }

    @PostMapping("/alarm")
    public ResponseEntity createAlarm() {
		MemberAlarmRequestDto memberAlarmRequestDto = null;
        memberService.addAlarm(memberAlarmRequestDto);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/alarms")
    public ResponseEntity<List<MemberAlarmResponseDto>> getAlarms() {
        return ResponseEntity.ok(memberService.getAlarms());
    }

    @PutMapping("/alarm/isRead")
    public ResponseEntity updateAlarmRead(@RequestParam Long alarmNo) {
        memberService.updateAlarmRead(alarmNo);
        return ResponseEntity.ok().build();
    }
}
