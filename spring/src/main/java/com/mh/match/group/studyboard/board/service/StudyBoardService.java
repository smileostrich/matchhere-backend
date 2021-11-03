package com.mh.match.group.studyboard.board.service;

import com.mh.match.common.entity.GroupAuthority;
import com.mh.match.common.exception.CustomException;
import com.mh.match.common.exception.ErrorCode;
import com.mh.match.group.studyboard.article.entity.StudyArticle;
import com.mh.match.group.studyboard.article.entity.StudyContent;
import com.mh.match.group.studyboard.article.repository.StudyArticleRepository;
import com.mh.match.group.studyboard.article.repository.StudyArticleTagRepository;
import com.mh.match.group.studyboard.article.repository.StudyContentRepository;
import com.mh.match.group.studyboard.board.entity.StudyBoard;
import com.mh.match.group.studyboard.comment.repository.StudyArticleCommentRepository;
import com.mh.match.member.entity.Member;
import com.mh.match.member.repository.MemberRepository;
import com.mh.match.group.study.entity.CompositeMemberStudy;
import com.mh.match.group.study.entity.MemberStudy;
import com.mh.match.group.study.entity.Study;
import com.mh.match.group.study.repository.MemberStudyRepository;
import com.mh.match.group.study.repository.StudyRepository;
import com.mh.match.group.studyboard.board.dto.StudyBoardCreateRequestDto;
import com.mh.match.group.studyboard.board.dto.StudyBoardInfoDto;
import com.mh.match.group.studyboard.board.dto.StudyBoardUpdateDto;
import com.mh.match.group.studyboard.board.repository.StudyBoardRepository;
import com.mh.match.util.SecurityUtil;
import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class StudyBoardService {

    private final StudyBoardRepository studyBoardRepository;
    private final StudyRepository studyRepository;
    private final StudyArticleRepository studyArticleRepository;
    private final StudyContentRepository studyContentRepository;
    private final StudyArticleTagRepository studyArticleTagRepository;
    private final StudyArticleCommentRepository studyArticleCommentRepository;
    private final MemberRepository memberRepository;
    private final MemberStudyRepository memberStudyRepository;

    @Transactional(readOnly = true)
    public List<StudyBoardInfoDto> getStudyBoards(Long studyId) {
        Study study = findStudy(studyId);
        Member member = findMember(SecurityUtil.getCurrentMemberId());

        // 가입 여부 확인
        checkAuthority(study, member);

        return studyBoardRepository.findAllByStudy(study).stream()
                .map(StudyBoardInfoDto::from)
                .collect(Collectors.toList());
    }

    @Transactional
    public StudyBoardInfoDto createBoard(Long studyId, StudyBoardCreateRequestDto dto) {
        Study study = findStudy(studyId);

        // 가입 여부 + 권한(관리자 or 소유자) 확인
        checkChangeAuthority(study, findMember(SecurityUtil.getCurrentMemberId()));

        return StudyBoardInfoDto.from(
                studyBoardRepository.save(StudyBoard.of(dto, study)));
    }

    @Transactional // 권한 확인 로직 필요
    public HttpStatus deleteBoard(Integer boardId) {
        StudyBoard studyBoard = findBoard(boardId);

        // 가입 여부 + 권한(관리자 or 소유자) 확인
        Study study = studyBoard.getStudy();
        checkChangeAuthority(study, findMember(SecurityUtil.getCurrentMemberId()));

        List<StudyArticle> studyArticles = studyArticleRepository.findAllByStudyBoard(studyBoard);

        for (StudyArticle studyArticle : studyArticles) {
            StudyContent studyContent = findStudyContent(studyArticle);
            studyContentRepository.delete(studyContent);
            deleteAllByStudyArticle(studyArticle);
            studyArticleTagRepository.deleteAllByStudyArticle(studyArticle);
            studyArticleRepository.delete(studyArticle);
        }

        studyBoardRepository.delete(studyBoardRepository.findById(boardId)
                .orElseThrow(() -> new CustomException(ErrorCode.BOARD_NOT_FOUND)));

        return HttpStatus.OK;
    }

    @Transactional  // 권한 확인 로직 필요
    public StudyBoardInfoDto updateBoard(Integer boardId, StudyBoardUpdateDto studyBoardUpdateDto) {
        StudyBoard studyBoard = findBoard(boardId);

        // 권한 체크 (팀원이면 x)
        checkChangeAuthority(studyBoard.getStudy(), findMember(SecurityUtil.getCurrentMemberId()));

        studyBoard.setName(studyBoardUpdateDto.getName());
        return StudyBoardInfoDto.from(studyBoard);
    }

    // 클럽 찾기
    public Study findStudy(Long studyId) {
        Study study = studyRepository.findById(studyId)
                .orElseThrow(() -> new CustomException(ErrorCode.CLUB_NOT_FOUND));

        if (Boolean.FALSE.equals(study.getIsActive())) {
            throw new CustomException(ErrorCode.DELETED_CLUB);
        }
        return study;
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
    public StudyBoard findBoard(int boardId) {
        return studyBoardRepository.findById(boardId)
                .orElseThrow(() -> new CustomException(ErrorCode.BOARD_NOT_FOUND));
    }

    public StudyArticle findStudyArticle(Long studyArticleId) {
        return studyArticleRepository.findById(studyArticleId)
                .orElseThrow(() -> new CustomException(ErrorCode.ARTICLE_NOT_FOUND));
    }

    public StudyContent findStudyContent(StudyArticle studyArticle) {
        return studyContentRepository.getByStudyArticle(studyArticle)
                .orElseThrow(() -> new CustomException(ErrorCode.CONTENT_NOT_FOUND));
    }

    public void deleteAllByStudyArticle(StudyArticle studyArticle) {
        studyArticleCommentRepository.deleteAllByStudyArticle(studyArticle);
    }

    public void checkAuthority(Study study, Member member){
        CompositeMemberStudy compositeMemberStudy = new CompositeMemberStudy(member, study);
        // 가입 여부 확인
        MemberStudy memberStudy = memberStudyRepository.findById(compositeMemberStudy)
                .orElseThrow(() -> new CustomException(ErrorCode.MEMBER_CLUB_NOT_FOUND));
        if (Boolean.FALSE.equals(memberStudy.getIsActive())) throw new CustomException(ErrorCode.MEMBER_CLUB_NOT_FOUND);

    }

    public void checkChangeAuthority(Study study, Member member){

        CompositeMemberStudy compositeMemberStudy = new CompositeMemberStudy(member, study);
        // 가입 여부 확인
        MemberStudy memberStudy = memberStudyRepository.findById(compositeMemberStudy)
                .orElseThrow(() -> new CustomException(ErrorCode.MEMBER_CLUB_NOT_FOUND));
        if (Boolean.FALSE.equals(memberStudy.getIsActive())) throw new CustomException(ErrorCode.MEMBER_CLUB_NOT_FOUND);

        // 팀원이면 권한 x
        if (memberStudy.getAuthority().equals(GroupAuthority.팀원)) throw new CustomException(ErrorCode.UNAUTHORIZED_CHANGE);

    }
}