package com.mh.match.member.service;

import com.mh.match.common.entity.Level;
import com.mh.match.common.entity.RefreshToken;
import com.mh.match.common.entity.Techstack;
import com.mh.match.common.repository.RefreshTokenRepository;
import com.mh.match.common.repository.TechstackRepository;
import com.mh.match.jwt.TokenProvider;
import com.mh.match.member.dto.MemberResponseDto;
import com.mh.match.member.dto.request.*;
import com.mh.match.member.entity.EmailCheck;
import com.mh.match.member.entity.composite.CompositeMemberTechstack;
import com.mh.match.member.repository.EmailCheckRepository;
import com.mh.match.member.repository.MemberRepository;
import com.mh.match.member.repository.MemberTechstackRepository;
import com.mh.match.member.dto.response.LoginResponseDto;
import com.mh.match.member.entity.MemberTechstack;
import com.mh.match.member.entity.Member;
import com.mh.match.common.entity.DetailPosition;
import com.mh.match.common.repository.DetailPositionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.mail.MailException;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.mail.internet.MimeMessage;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
@RequiredArgsConstructor
public class AuthService {
    private final AuthenticationManagerBuilder authenticationManagerBuilder;
    private final MemberRepository memberRepository;
    private final PasswordEncoder passwordEncoder;
    private final TokenProvider tokenProvider;
    private final RefreshTokenRepository refreshTokenRepository;
    private final TechstackRepository techstackRepository;
    private final DetailPositionRepository detailPositionRepository;
    private final MemberTechstackRepository memberTechstackRepository;
    private final EmailCheckRepository emailCheckRepository;
    private final JavaMailSender javaMailSender;

//    @Value("${spring.mail.username}")
//    private String from;

    private MimeMessage createMessage(String to, String key) throws Exception {
        MimeMessage message = javaMailSender.createMimeMessage();

        message.addRecipients(MimeMessage.RecipientType.TO, to); //보내는 대상
        message.setSubject("MatchHere 메일 인증 코드"); //제목

        String msg = "";
        msg += "<table border=\"0\" cellpadding=\"0\" cellspacing=\"0\" width=\"100%\" style=\"font-family:sans-serif;color:#333333;padding:0;font-size:16px\">";
        msg += "<tbody><tr style=\"color:#333333;padding:0;font-size:16px\">";
        msg += "<td align=\"center\" style=\"color:#333333;padding:0;font-size:16px\">";
        msg += "<table border=\"0\" cellpadding=\"0\" cellspacing=\"0\" width=\"600\" style=\"color:#333333;padding:0;font-size:16px\">";
        msg += "<tbody>";
        msg += "<tr style=\"color:#333333;padding:0;font-size:16px\">";
        msg += "<td style=\"color:#333333;padding:0;font-size:16px\">";
        msg += "<table border=\"0\" cellpadding=\"0\" cellspacing=\"0\" style=\"width:100%;color:#333333;padding:0;font-size:16px\">";
        msg += "<tbody><tr style=\"color:#333333;padding:0;font-size:16px\">";
        msg += "<td id=\"m_-5217547372947520329title\" align=\"center\" style=\"color:#333333;padding:0;font-size:24px;font-weight:bold;padding-top:48px\">";
        msg += "이메일 인증 코드";
        msg += "</td>";
        msg += "</tr>";
        msg += "<tr>";
        msg += "<td style=\"text-align:center;padding-top:64px\">";
        msg += "<table align=\"center\" style=\"width:374px;height:98px;background-color:#f1f2f6;font-size:48px;font-weight:bold;text-align:center;border-radius:4px;margin-left:auto;margin-right:auto\" width=\"374\" height=\"98\" bgcolor=\"#f1f2f6\">";
        msg += "<tbody><tr>";
        msg += "<td><span>" + key + "</span></td>";
        msg += "</tr>";
        msg += "</tbody></table>";
        msg += "</td>";
        msg += "</tr>";
        msg += "<tr style=\"color:#333333;padding:0;font-size:16px\">";
        msg += "<td style=\"color:#333333;width:100%;padding:0;font-size:16px;padding-top:48px;padding-bottom:64px;text-align:center\">";
        msg += "이메일 인증 코드가 발급되었습니다.";
        msg += "<br>";
        msg += "해당 문자를 인증 코드 입력창에 입력해 주세요.";
        msg += "</td>";
        msg += "</tr>";
        msg += "</tbody></table>";
        msg += "</td>";
        msg += "</tr>";
        msg += "";
        msg += "<tr style=\"color:#333333;padding:0;font-size:16px\">";
        msg += "<td style=\"color:#333333;padding:0;font-size:16px;border-top-width:1px;border-top-style:solid;border-top-color:#cccccc;padding-top:16px;padding-left:8px\">";
        msg += "<table border=\"0\" cellpadding=\"0\" cellspacing=\"0\" style=\"color:#333333;padding:0;font-size:16px\">";
        msg += "<tbody><tr style=\"color:#333333;padding:0;font-size:16px\">";
        msg += "<td height=\"0\" style=\"color:#666666;font-size:12px;padding-bottom:22px;height:0;line-height:0\">MatchHere</td>";
        msg += "</tr>";
        msg += "<tr style=\"color:#333333;padding:0;font-size:16px\">";
        msg += "<td style=\"padding:0;color:#666666;font-size:12px;padding-bottom:28px\">";
        msg += "<a href=\"https://matchhere.me\" style=\"text-decoration:none\">HOME</a>";
        msg += "</td>";
        msg += "</tr>";
        msg += "<tr style=\"color:#333333;padding:0;font-size:16px\">";
        msg += "<td style=\"padding:0;color:#666666;font-size:12px\">";
        msg += "Copyright 2021. MatchHere. All rights reserved.";
        msg += "</td>";
        msg += "</tr>";
        msg += "</tbody></table>";
        msg += "</td>";
        msg += "</tr>";
        msg += "</tbody></table>";
        msg += "</td>";
        msg += "</tr>";
        msg += "</tbody></table>";

        message.setText(msg, "utf-8", "html"); //내용
//        message.setFrom(new InternetAddress("보내는 계정","ian.ilminmoon")); //보내는 사람

        return message;
    }

    @Transactional
    public Long certSignup(String email) throws Exception {
        Optional<Member> member = memberRepository.findByEmail(email);
        if (member.isPresent()) {
            return -1L;
        } else {
            String key = certified_key();
//            SimpleMailMessage message = new SimpleMailMessage();
//            message.setTo(email);
//            message.setFrom(from);
//            message.setSubject("이메일 인증");

            MimeMessage message = createMessage(email, key);
            try{//예외처리
                javaMailSender.send(message);
            }catch(MailException es){
                es.printStackTrace();
                throw new IllegalArgumentException();
            }
//            message.setText(msg);
//            javaMailSender.send(message);
//            Optional<EmailCheck> emailCheck = emailCheckRepository.findByEmail(email);
//            if(emailCheck.isEmpty()) {
            EmailCheck check = new EmailCheck(email, key, Boolean.FALSE);
            emailCheckRepository.save(check);
//            } else {
//                emailCheck.get().updateKey(key);
//            }
            return check.getId();
        }
    }

    @Transactional
    public Long certPassword(String email) throws Exception {
        Optional<Member> member = memberRepository.findByEmail(email);
        if (!member.isPresent()) {
            return -1L;
        } else {
            String key = certified_key();
//            SimpleMailMessage message = new SimpleMailMessage();
//            message.setTo(email);
//            message.setFrom(new InternetAddress("보내는 계정","ian.ilminmoon@gmail.com"));
//            message.setSubject("MatchHere 메일 인증 코드");

            MimeMessage message = createMessage(email, key);
            try{//예외처리
                javaMailSender.send(message);
            }catch(MailException es){
                es.printStackTrace();
                throw new IllegalArgumentException();
            }

//            message.setText(msg);
//            javaMailSender.send(message);
//            Optional<EmailCheck> emailCheck = emailCheckRepository.findByEmail(email);
//            if(emailCheck.isEmpty()) {
            EmailCheck check = new EmailCheck(email, key, Boolean.FALSE);
            emailCheckRepository.save(check);
//            } else {
//                emailCheck.get().updateKey(key);
//            }
            return check.getId();
        }
    }

    @Transactional
    public Long emailAuthCode(Long id, EmailCertRequestDto emailCertRequestDto) throws Exception {
//        Optional<EmailCheck> emailCheck = emailCheckRepository.findByEmail(emailCertRequestDto.getEmail());
        EmailCheck emailCheck = emailCheckRepository.findById(id).orElseThrow(() -> new Exception("인증 id가 존재하지 않습니다!"));
        if (emailCheck.getAuthCode().equals(emailCertRequestDto.getAuthCode())) {
            emailCheck.updateIsCheck(Boolean.TRUE);
            return emailCheck.getId();
        }
        return -1L;
    }

    @Transactional(readOnly = true)
    public Boolean checkNickname(String nickname) {
        if (memberRepository.existsByNickname(nickname)) {
            return Boolean.TRUE;
        }
        return Boolean.FALSE;
    }

    @Transactional
    public HttpStatus changePassword(ForgetChangePasswordRequestDto forgetChangePasswordRequestDto) throws Exception {
        EmailCheck emailCheck = emailCheckRepository.findById(forgetChangePasswordRequestDto.getId()).orElseThrow(() -> new NullPointerException("잘못된 이메일 인증 id입니다!"));
        if (!forgetChangePasswordRequestDto.getEmail().equals(emailCheck.getEmail())) {
            throw new Exception("잘못된 접근입니다! 요청한 email과 인증한 이메일이 다릅니다!");
        }
        if (emailCheck.getIs_check().equals(Boolean.FALSE)) {
            throw new Exception("email인증이 완료되지 않았습니다!");
        }
        Member member = memberRepository.findByEmail(emailCheck.getEmail())
                .orElseThrow(() -> new NullPointerException("가입되어있지 않은 이메일입니다."));
        forgetChangePasswordRequestDto.setPassword(member, passwordEncoder);
        emailCheckRepository.deleteById(forgetChangePasswordRequestDto.getId());
        return HttpStatus.OK;
    }

    @Transactional
    public MemberResponseDto signup(SignupRequestDto signupRequestDto) throws Exception {
        EmailCheck emailCheck = emailCheckRepository.findById(signupRequestDto.getId()).orElseThrow(() -> new NullPointerException("잘못된 이메일 인증 id입니다!"));
        if (emailCheck.getIs_check().equals(Boolean.FALSE)) {
            throw new Exception("email인증이 완료되지 않았습니다!");
        }
        if (memberRepository.existsByEmail(emailCheck.getEmail())) {
            throw new Exception("이미 가입되어 있는 유저입니다");
        }
        if (memberRepository.existsByNickname(signupRequestDto.getNickname())) {
            throw new Exception("중복된 닉네임 입니다");
        }
        if (signupRequestDto.getIsAgree().equals(Boolean.FALSE) || signupRequestDto.getIsPAgree().equals(Boolean.FALSE)) {
            throw new Exception("약관에 동의 하지 않은 사용자입니다!");
        }

        Member member = signupRequestDto.toMember(passwordEncoder, emailCheck.getEmail());
        Member ret = memberRepository.save(member);

        if (signupRequestDto.getDpositionList() != null) {
            addDetailPosition(signupRequestDto.getDpositionList(), ret);
        }
        if (signupRequestDto.getTechList() != null) {
            addTechList(signupRequestDto.getTechList(), ret);
        }
        emailCheckRepository.deleteById(signupRequestDto.getId());
        return MemberResponseDto.of(ret);
    }

    @Transactional
    public LoginResponseDto login(LoginRequestDto loginRequestDto) throws Exception{
        // 1. Login ID/PW 를 기반으로 AuthenticationToken 생성
        UsernamePasswordAuthenticationToken authenticationToken = loginRequestDto.toAuthentication();
        // 2. 실제로 검증 (사용자 비밀번호 체크) 이 이루어지는 부분
        //    authenticate 메서드가 실행이 될 때 CustomUserDetailsService 에서 만들었던 loadUserByUsername 메서드가 실행됨
        Authentication authentication = authenticationManagerBuilder.getObject().authenticate(authenticationToken);
        //탈퇴회원 체크
        Member member = memberRepository.getById(Long.parseLong(authentication.getName()));
        if (!member.getIs_active()) {
            throw new Exception("탈퇴한 회원입니다!");
        }
        // 3. 인증 정보를 기반으로 JWT 토큰 생성
        TokenDto tokenDto = tokenProvider.generateTokenDto(authentication);

        // 4. RefreshToken 저장
        RefreshToken refreshToken = RefreshToken.builder()
                .key(authentication.getName())
                .value(tokenDto.getRefreshToken())
                .build();
        refreshTokenRepository.save(refreshToken);

        LoginResponseDto loginResponseDto = LoginResponseDto.of(member, tokenDto);
        return loginResponseDto;
    }

    @Transactional
    public TokenDto reissue(TokenRequestDto tokenRequestDto) {
        // 1. Refresh Token 검증
        if (!tokenProvider.validateToken(tokenRequestDto.getRefreshToken())) {
            throw new RuntimeException("Refresh Token 이 유효하지 않습니다.");
        }
        // 2. Access Token 에서 Member ID 가져오기
        Authentication authentication = tokenProvider.getAuthentication(tokenRequestDto.getAccessToken());

        // 3. 저장소에서 Member ID 를 기반으로 Refresh Token 값 가져옴
        RefreshToken refreshToken = refreshTokenRepository.findByKey(authentication.getName())
                .orElseThrow(() -> new RuntimeException("로그아웃 된 사용자입니다."));
        // 4. Refresh Token 일치하는지 검사
        if (!refreshToken.getValue().equals(tokenRequestDto.getRefreshToken())) {
            throw new RuntimeException("토큰의 유저 정보가 일치하지 않습니다.");
        }
        // 5. 새로운 토큰 생성
        TokenDto tokenDto = tokenProvider.generateTokenDto(authentication);
        // 6. 저장소 정보 업데이트

        RefreshToken newRefreshToken = refreshToken.updateValue(tokenDto.getRefreshToken());
        refreshTokenRepository.save(newRefreshToken);
        // 토큰 발급
        return tokenDto;
    }

    @Transactional
    public void addDetailPosition(List<String> dPositionList, Member member) {
        for (String dPosition : dPositionList) {
            DetailPosition innerDposition = DetailPosition
                    .builder()
                    .member(member)
                    .name(dPosition)
                    .build();
            detailPositionRepository.save(innerDposition);
        }
    }

    @Transactional
    public void addTechList(HashMap<String, String> techList, Member member) throws Exception {
        for (Map.Entry<String, String> entry : techList.entrySet()) {
            Techstack techstack = techstackRepository.findByName(entry.getKey())
                    .orElseThrow(() -> new NullPointerException("기술 스택 정보가 없습니다."));
            validLevel(entry.getValue());
            CompositeMemberTechstack compositeMemberTechstack = CompositeMemberTechstack
                    .builder()
                    .member(member)
                    .techstack(techstack)
                    .build();
            MemberTechstack memberTechstack = MemberTechstack.builder().compositeMemberTechstack(compositeMemberTechstack).level(entry.getValue()).build();
            memberTechstackRepository.save(memberTechstack);
        }
    }

    @Transactional(readOnly = true)
    public void validLevel(String level) throws Exception {
        if (!Stream.of(Level.values()).map(Enum::name)
                .collect(Collectors.toList()).contains(level)) {
            throw new Exception("존재하지 않는 level입니다");
        }
    }

    private String certified_key() { //10자리 임의의 문자열을 만드는 함수
        Random random = new Random();
        StringBuffer sb = new StringBuffer();
        int num = 0;

        do {
            num = random.nextInt(75) + 48; //0<=num<75
            if ((num >= 48 && num <= 57) || (num >= 65 && num <= 90) || (num >= 97 && num <= 122)) {
                sb.append((char) num);
            } else {
                continue;
            }

        } while (sb.length() < 10);
        return sb.toString();
    }
}