package com.mh.match.group.clubboard.board.service;

import com.mh.match.common.entity.GroupAuthority;
import com.mh.match.common.exception.CustomException;
import com.mh.match.common.exception.ErrorCode;
import com.mh.match.group.clubboard.article.entity.ClubArticle;
import com.mh.match.group.clubboard.article.entity.ClubContent;
import com.mh.match.group.clubboard.article.repository.ClubArticleRepository;
import com.mh.match.group.clubboard.article.repository.ClubArticleTagRepository;
import com.mh.match.group.clubboard.article.repository.ClubContentRepository;
import com.mh.match.group.clubboard.board.dto.ClubBoardInfoDto;
import com.mh.match.group.clubboard.board.entity.ClubBoard;
import com.mh.match.group.clubboard.board.repository.ClubBoardRepository;
import com.mh.match.group.clubboard.comment.repository.ClubArticleCommentRepository;
import com.mh.match.member.entity.Member;
import com.mh.match.member.repository.MemberRepository;
import com.mh.match.group.club.entity.Club;
import com.mh.match.group.club.entity.CompositeMemberClub;
import com.mh.match.group.club.entity.MemberClub;
import com.mh.match.group.club.repository.ClubRepository;
import com.mh.match.group.club.repository.MemberClubRepository;
import com.mh.match.group.clubboard.board.dto.ClubBoardCreateRequestDto;
import com.mh.match.group.clubboard.board.dto.ClubBoardUpdateDto;

import java.util.List;
import java.util.stream.Collectors;

import com.mh.match.util.SecurityUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class ClubBoardService {
    private final ClubBoardRepository clubBoardRepository;
    private final ClubRepository clubRepository;
    private final ClubArticleRepository clubArticleRepository;
    private final ClubContentRepository clubContentRepository;
    private final ClubArticleTagRepository clubArticleTagRepository;
    private final ClubArticleCommentRepository clubArticleCommentRepository;
    private final MemberRepository memberRepository;
    private final MemberClubRepository memberClubRepository;

    @Transactional(readOnly = true)
    public List<ClubBoardInfoDto> getClubBoards(Long clubId) {
        Club club = findClub(clubId);
        Member member = findMember(SecurityUtil.getCurrentMemberId());

        // 가입 여부 확인
        checkAuthority(club, member);

        return clubBoardRepository.findAllByClub(club).stream()
                .map(ClubBoardInfoDto::from)
                .collect(Collectors.toList());
    }

    @Transactional
    public ClubBoardInfoDto createBoard(Long clubId, ClubBoardCreateRequestDto dto) {
        Club club = findClub(clubId);

        // 가입 여부 + 권한(관리자 or 소유자) 확인
        checkChangeAuthority(club, findMember(SecurityUtil.getCurrentMemberId()));

        return ClubBoardInfoDto.from(
                clubBoardRepository.save(ClubBoard.of(dto, club)));
    }

    @Transactional // 권한 확인 로직 필요
    public HttpStatus deleteBoard(Integer boardId) {
        ClubBoard clubBoard = findBoard(boardId);

        // 가입 여부 + 권한(관리자 or 소유자) 확인
        Club club = clubBoard.getClub();
        checkChangeAuthority(club, findMember(SecurityUtil.getCurrentMemberId()));

        List<ClubArticle> clubArticles = clubArticleRepository.findAllByClubBoard(clubBoard);

        for (ClubArticle clubArticle : clubArticles) {
            ClubContent clubContent = findClubContent(clubArticle);
            clubContentRepository.delete(clubContent);
            deleteAllByClubArticle(clubArticle);
            clubArticleTagRepository.deleteAllByClubArticle(clubArticle);
            clubArticleRepository.delete(clubArticle);
        }

        clubBoardRepository.delete(clubBoardRepository.findById(boardId)
                .orElseThrow(() -> new CustomException(ErrorCode.BOARD_NOT_FOUND)));

        return HttpStatus.OK;
    }

    @Transactional  // 권한 확인 로직 필요
    public ClubBoardInfoDto updateBoard(Integer boardId, ClubBoardUpdateDto clubBoardUpdateDto) {
        ClubBoard clubBoard = findBoard(boardId);

        // 권한 체크 (팀원이면 x)
        checkAuthority(clubBoard.getClub(), findMember(SecurityUtil.getCurrentMemberId()));

        clubBoard.setName(clubBoardUpdateDto.getName());
        return ClubBoardInfoDto.from(clubBoard);
    }

    // 클럽 찾기
    public Club findClub(Long clubId) {
        Club club = clubRepository.findById(clubId)
                .orElseThrow(() -> new CustomException(ErrorCode.CLUB_NOT_FOUND));

        if (Boolean.FALSE.equals(club.getIsActive())) {
            throw new CustomException(ErrorCode.DELETED_CLUB);
        }
        return club;
    }

    public Member findMember(Long memberId) {
        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new CustomException(ErrorCode.MEMBER_NOT_FOUND));
        if (!member.getIs_active()) {
            throw new CustomException(ErrorCode.MEMBER_NOT_FOUND);
        }
        return member;
    }

    // 게시판 찾기
    public ClubBoard findBoard(int boardId) {
        return clubBoardRepository.findById(boardId)
                .orElseThrow(() -> new CustomException(ErrorCode.BOARD_NOT_FOUND));
    }

    public ClubArticle findClubArticle(Long clubArticleId) {
        return clubArticleRepository.findById(clubArticleId)
                .orElseThrow(() -> new CustomException(ErrorCode.ARTICLE_NOT_FOUND));
    }

    public ClubContent findClubContent(ClubArticle clubArticle) {
        return clubContentRepository.getByClubArticle(clubArticle)
                .orElseThrow(() -> new CustomException(ErrorCode.CONTENT_NOT_FOUND));
    }

    public void deleteAllByClubArticle(ClubArticle clubArticle) {
        clubArticleCommentRepository.deleteAllByClubArticle(clubArticle);
    }

    public void checkAuthority(Club club, Member member){
        CompositeMemberClub compositeMemberClub = new CompositeMemberClub(member, club);
        // 가입 여부 확인
        MemberClub memberClub = memberClubRepository.findById(compositeMemberClub)
                .orElseThrow(() -> new CustomException(ErrorCode.MEMBER_CLUB_NOT_FOUND));
        if (Boolean.FALSE.equals(memberClub.getIsActive())) throw new CustomException(ErrorCode.MEMBER_CLUB_NOT_FOUND);

    }

    public void checkChangeAuthority(Club club, Member member){

        CompositeMemberClub compositeMemberClub = new CompositeMemberClub(member, club);
        // 가입 여부 확인
        MemberClub memberClub = memberClubRepository.findById(compositeMemberClub)
                .orElseThrow(() -> new CustomException(ErrorCode.MEMBER_CLUB_NOT_FOUND));
        if (Boolean.FALSE.equals(memberClub.getIsActive())) throw new CustomException(ErrorCode.MEMBER_CLUB_NOT_FOUND);

        // 팀원이면 권한 x
        if (memberClub.getAuthority().equals(GroupAuthority.팀원)) throw new CustomException(ErrorCode.UNAUTHORIZED_CHANGE);

    }
}