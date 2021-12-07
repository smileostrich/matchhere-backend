package com.mh.match.portfolio.service;

import com.mh.match.portfolio.dto.request.PortfolioCreateRequestDto;
import com.mh.match.portfolio.dto.request.PortfolioUpdateRequestDto;
import com.mh.match.portfolio.dto.response.PortfolioInfoForUpdateResponseDto;
import com.mh.match.portfolio.dto.response.PortfolioInfoResponseDto;
import com.mh.match.s3.dto.DBFileDto;
import org.springframework.http.HttpStatus;
import org.springframework.web.multipart.MultipartFile;

public interface PortfolioService {

    //클럽 생성
    PortfolioInfoResponseDto create(MultipartFile file, PortfolioCreateRequestDto dto);

    // 클럽 수정
    PortfolioInfoResponseDto update(Long portfolioId, PortfolioUpdateRequestDto dto);

    // 클럽 수정시 사진 변경
    DBFileDto changeCoverPic(MultipartFile file, Long portfolioId);

//    DBFileDto getCoverPicUri(Long clubId);

    // 조회 수 증가
    HttpStatus plusViewCount(Long portfolioId);

    HttpStatus delete(Long portfolioId);

    // 클럽 전체 조회
//    Page<ClubSimpleInfoResponseDto> getAllClub(Pageable pageable);

    // 클럽 상세 조회
    PortfolioInfoResponseDto getOnePortfolio(Long portfolioId);

    // 현재 클럽 간편 정보 리턴
//    ClubSimpleInfoResponseDto getOneSimpleClub(Long clubId);

    PortfolioInfoForUpdateResponseDto getInfoForUpdatePortfolio(Long portfolioId);
}
