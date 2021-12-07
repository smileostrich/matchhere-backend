package com.mh.match.portfolio.service;


import com.mh.match.common.exception.CustomException;
import com.mh.match.common.exception.ErrorCode;
import com.mh.match.member.entity.Member;
import com.mh.match.member.repository.MemberRepository;
import com.mh.match.portfolio.dto.request.PortfolioCreateRequestDto;
import com.mh.match.portfolio.dto.request.PortfolioUpdateRequestDto;
import com.mh.match.portfolio.entity.*;
import com.mh.match.portfolio.repository.PortfolioRepository;
import com.mh.match.portfolio.repository.PortfolioTagRepository;
import com.mh.match.portfolio.dto.response.PortfolioInfoForUpdateResponseDto;
import com.mh.match.portfolio.dto.response.PortfolioInfoResponseDto;
import com.mh.match.portfolio.dto.response.PortfolioTagResponseDto;
import com.mh.match.s3.dto.DBFileDto;
import com.mh.match.s3.entity.DBFile;
import com.mh.match.s3.repository.DBFileRepository;
import com.mh.match.s3.service.S3Service;
import com.mh.match.util.SecurityUtil;

import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

@Service
@Transactional(readOnly = true, rollbackFor = Exception.class)
@RequiredArgsConstructor
public class PortfolioServiceImpl implements PortfolioService {

    private final MemberRepository memberRepository;
    private final PortfolioRepository portfolioRepository;
    private final DBFileRepository dbFileRepository;
    private final PortfolioTagRepository portfolioTagRepository;
    private final S3Service s3Service;

    // 포트폴리오 생성
    @Transactional
    public PortfolioInfoResponseDto create(MultipartFile file, PortfolioCreateRequestDto dto) {
        Member member = findMember(SecurityUtil.getCurrentMemberId());
        Portfolio portfolio = Portfolio.of(dto, member);
        portfolioRepository.save(portfolio);
        if (file != null) {
            DBFile dbFile = s3Service.uploadFile(file, "portfolio/" + Long.toString(portfolio.getId()) + "/cover/");
            portfolio.setCoverPic(dbFile);
        }
        addTags(portfolio, dto.getTags());

        return PortfolioInfoResponseDto.of(portfolio, getPortfolioTags(portfolio));
    }

    // 포트폴리오 업데이트를 위한 정보
    public PortfolioInfoForUpdateResponseDto getInfoForUpdatePortfolio(Long portfolioId) {
        Portfolio portfolio = findPortfolio(portfolioId);
        if (!SecurityUtil.getCurrentMemberId().equals(portfolio.getMember().getId())) {
            throw new CustomException(ErrorCode.UNAUTHORIZED_CHANGE);
        }
        return PortfolioInfoForUpdateResponseDto.of(portfolio, getPortfolioTags(portfolio));
    }

    // 포트폴리오 주제 리스트
    private List<PortfolioTagResponseDto> getPortfolioTags(Portfolio portfolio) {
        return portfolioTagRepository.findAllByPortfolio(portfolio)
                .stream().map(PortfolioTagResponseDto::from).collect(Collectors.toList());
    }

    // 포트폴리오 업데이트
    @Transactional
    public PortfolioInfoResponseDto update(Long portfolioId, PortfolioUpdateRequestDto dto) {
        Portfolio portfolio = findPortfolio(portfolioId);

        if (!SecurityUtil.getCurrentMemberId().equals(portfolio.getMember().getId())) {
            throw new CustomException(ErrorCode.UNAUTHORIZED_CHANGE);
        }

        portfolio.update(dto);
        addTags(portfolio, dto.getTags());

        return PortfolioInfoResponseDto.of(portfolio, getPortfolioTags(portfolio));
    }

    // 사진 바꾸기
    @Transactional
    public DBFileDto changeCoverPic(MultipartFile file, Long portfolioId) {
        if (file == null) {
            return DBFileDto.of(null);
        }
        Portfolio portfolio = findPortfolio(portfolioId);

        // 권한 체크
        if (!SecurityUtil.getCurrentMemberId().equals(portfolio.getMember().getId())) {
            throw new CustomException(ErrorCode.UNAUTHORIZED_CHANGE);
        }

        if (portfolio.getCoverPic() != null) {
            s3Service.deleteS3File("portfolio/" + Long.toString(portfolioId) + "/cover/" + portfolio.getCoverPic().getId());
        }
        DBFile dbFile = s3Service.uploadFile(file, "portfolio/" + Long.toString(portfolioId) + "/cover/");
        portfolio.setCoverPic(dbFile);
        return DBFileDto.of(dbFile);
    }

    // 사진 정보만 가져오기
//    public DBFileDto getCoverPicUri(Long clubId) {
//        return DBFileDto.of(findClub(clubId).getCoverPic());
//    }

    // 포트폴리오 조회수 증가
    @Transactional
    public HttpStatus plusViewCount(Long portfolioId) {
        findPortfolio(portfolioId).plusViewCount();
        return HttpStatus.OK;
    }

    @Transactional
    public HttpStatus delete(Long portfolioId) {
        Portfolio portfolio = findPortfolio(portfolioId);

        if (!SecurityUtil.getCurrentMemberId().equals(portfolio.getMember().getId())) {
            throw new CustomException(ErrorCode.UNAUTHORIZED_CHANGE);
        }

        // 클럽 주제 제거
        portfolioTagRepository.deleteAllByPortfolio(portfolio);

        // 클럽 Cover 제거
        if (portfolio.getCoverPic() != null) {
            s3Service.deleteS3File("portfolio/" + Long.toString(portfolioId) + "/cover/"+ portfolio.getCoverPic().getId());
            String uuid = portfolio.getCoverPic().getId();
            dbFileRepository.deleteById(uuid);
            portfolio.initialCoverPic();
        }

        portfolioRepository.delete(portfolio);
        return HttpStatus.OK;
    }

    // 클럽 전체 조회
//    public Page<ClubSimpleInfoResponseDto> getAllClub(Pageable pageable) {
//        return portfolioRepository.findAllClub(RecruitmentState.RECRUITMENT,
//                        PublicScope.PUBLIC, pageable)
//                .map(m -> ClubSimpleInfoResponseDto.of(m, getPortfolioTags(m)));
//    }

    // 클럽 상세 조회
    public PortfolioInfoResponseDto getOnePortfolio(Long portfolioId) {
        Portfolio portfolio = findPortfolio(portfolioId);

        return PortfolioInfoResponseDto.of(portfolio, getPortfolioTags(portfolio));
    }

    // 현재 클럽 간편 정보 리턴
//    public ClubSimpleInfoResponseDto getOneSimpleClub(Long clubId) {
//        Portfolio club = findClub(clubId);
//        return ClubSimpleInfoResponseDto.of(club, getPortfolioTags(club));
//    }

    // 클럽 주제 추가, 변경
    @Transactional
    public void addTags(Portfolio portfolio, List<String> tags) {
        portfolioTagRepository.deleteAllByPortfolio(portfolio);
        if (tags.isEmpty()) {
            return;
        }
        for (String tag : tags) {
            portfolioTagRepository.save(PortfolioTag.of(portfolio, tag));
        }
    }

    public Portfolio findPortfolio(Long portfolioId) {
        if (portfolioId == null) {
            return null;
        }
        return portfolioRepository.findById(portfolioId)
                .orElseThrow(() -> new CustomException(ErrorCode.CLUB_NOT_FOUND));
    }

    @Transactional(readOnly = true)
    public Member findMember(Long memberId) {
        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new CustomException(ErrorCode.MEMBER_NOT_FOUND));
        if (Boolean.FALSE.equals(member.getIs_active())) {
            throw new CustomException(ErrorCode.MEMBER_NOT_FOUND);
        }
        return member;
    }
}