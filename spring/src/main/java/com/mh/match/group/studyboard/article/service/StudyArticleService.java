package com.mh.match.group.studyboard.article.service;


import com.mh.match.common.entity.GroupAuthority;
import com.mh.match.common.exception.CustomException;
import com.mh.match.common.exception.ErrorCode;
import com.mh.match.group.studyboard.article.entity.StudyArticle;
import com.mh.match.group.studyboard.article.entity.StudyArticleTag;
import com.mh.match.group.studyboard.article.entity.StudyContent;
import com.mh.match.group.studyboard.article.repository.StudyArticleRepository;
import com.mh.match.group.studyboard.article.repository.StudyArticleTagRepository;
import com.mh.match.group.studyboard.article.repository.StudyContentRepository;
import com.mh.match.group.studyboard.comment.repository.StudyArticleCommentRepository;
import com.mh.match.member.entity.Member;
import com.mh.match.member.repository.MemberRepository;
import com.mh.match.group.study.entity.CompositeMemberStudy;
import com.mh.match.group.study.entity.MemberStudy;
import com.mh.match.group.study.entity.Study;
import com.mh.match.group.study.repository.MemberStudyRepository;
import com.mh.match.group.studyboard.article.dto.StudyArticleInfoResponseDto;
import com.mh.match.group.studyboard.article.dto.StudyArticleRequestDto;
import com.mh.match.group.studyboard.article.dto.StudyArticleSimpleInfoResponseDto;
import com.mh.match.group.studyboard.board.entity.StudyBoard;
import com.mh.match.group.studyboard.board.repository.StudyBoardRepository;
import com.mh.match.util.SecurityUtil;
import java.util.ArrayList;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class StudyArticleService {
    private final StudyBoardRepository studyBoardRepository;
    private final StudyArticleRepository studyArticleRepository;
    private final StudyContentRepository studyContentRepository;
    private final MemberRepository memberRepository;
    private final StudyArticleTagRepository studyArticleTagRepository;
    private final StudyArticleCommentRepository studyArticleCommentRepository;
    private final MemberStudyRepository memberStudyRepository;

    @Transactional(readOnly = true)
    public StudyArticleInfoResponseDto getStudyArticleDetail(Long articleId) {
        StudyArticle studyArticle = findStudyArticle(articleId);

        // 권한 체크 (소속원인지 확인)
        checkAuthority(studyArticle.getStudyBoard().getStudy(), findMember(SecurityUtil.getCurrentMemberId()));

        StudyArticleInfoResponseDto studyArticleInfoResponseDto = StudyArticleInfoResponseDto.of(
                studyArticle, getStudyArticleTagList(studyArticle));
        StudyContent studyContent = findStudyContent(studyArticle);
        studyArticleInfoResponseDto.setContent(studyContent.getContent());
        return studyArticleInfoResponseDto;
    }

    public List<String> getStudyArticleTagList(StudyArticle studyArticle) {
        List<StudyArticleTag> pats = studyArticleTagRepository.findAllByStudyArticle(
                studyArticle);
        List<String> tags = new ArrayList<>();
        for (StudyArticleTag pat : pats) {
            tags.add(pat.getName());
        }
        return tags;
    }

    @Transactional(readOnly = true)
    public Page<StudyArticleSimpleInfoResponseDto> getStudyArticles(Integer boardId, Pageable pageable) {
        StudyBoard studyBoard = findStudyBoard(boardId);
        // 권한 체크 (소속원인지 확인)
        checkAuthority(studyBoard.getStudy(), findMember(SecurityUtil.getCurrentMemberId()));

        Page<StudyArticle> studyArticles = studyArticleRepository.findAllByStudyBoard(studyBoard,
                pageable);
        return studyArticles.map(
                m -> StudyArticleSimpleInfoResponseDto.of(m, getStudyArticleTagList(m)));
    }

    @Transactional
    public StudyArticleInfoResponseDto createArticle(StudyArticleRequestDto dto) {
        StudyBoard studyBoard = findStudyBoard(dto.getStudyBoardId());
        // 권한 체크 (소속원인지 확인)
        Study study = studyBoard.getStudy();
        Member member = findMember(SecurityUtil.getCurrentMemberId());
        checkAuthority(study, member);

        if (dto.getContent() == null) {
            throw new CustomException(ErrorCode.CONTENT_NOT_FOUND);
        }
        StudyArticle studyArticle = studyArticleRepository.save(
                StudyArticle.of(dto, studyBoard, member));
        addContent(studyArticle, dto.getContent());
        addTags(studyArticle, dto.getTags());

        StudyArticleInfoResponseDto studyArticleInfoResponseDto = StudyArticleInfoResponseDto.of(
                studyArticle, dto.getTags());
        studyArticleInfoResponseDto.setContent(dto.getContent());
        return studyArticleInfoResponseDto;
    }

    @Transactional
    public StudyArticleInfoResponseDto updateArticle(Long articleId, StudyArticleRequestDto dto) {
        StudyArticle studyArticle = findStudyArticle(articleId);

        // 권한 체크 (소속원인지 확인)
        checkUpdateAuthority(studyArticle.getStudyBoard().getStudy(),
                findMember(SecurityUtil.getCurrentMemberId()), studyArticle);

        if (dto.getContent() == null) {
            throw new CustomException(ErrorCode.CONTENT_NOT_FOUND);
        }

        // 게시글 내용
        StudyContent studyContent = findStudyContent(studyArticle);
        studyContent.setContent(dto.getContent());

        studyArticle.update(dto, findStudyBoard(dto.getStudyBoardId()));
        addTags(studyArticle, dto.getTags());

        StudyArticleInfoResponseDto studyArticleInfoResponseDto = StudyArticleInfoResponseDto.of(
                studyArticle, dto.getTags());
        studyArticleInfoResponseDto.setContent(studyContent.getContent());
        return studyArticleInfoResponseDto;
    }

    @Transactional
    public HttpStatus deleteArticle(Long articleId) {
        StudyArticle studyArticle = findStudyArticle(articleId);

        // 소속원이고 작성자이거나 소유자, 관리자인지
        checkDeleteAuthority(studyArticle.getStudyBoard().getStudy(),
                findMember(SecurityUtil.getCurrentMemberId()), studyArticle);

        StudyContent studyContent = findStudyContent(studyArticle);
        deleteAllCommentByStudyArticle(studyArticle);
        studyArticleTagRepository.deleteAllByStudyArticle(studyArticle);
        studyContentRepository.delete(studyContent);
        studyArticleRepository.delete(studyArticle);

        return HttpStatus.OK;
    }

    @Transactional
    public void addContent(StudyArticle studyArticle, String content) {
        studyContentRepository.save(StudyContent.of(studyArticle, content));
    }

    // 클럽 게시글 조회수 증가
    @Transactional
    public HttpStatus plusViewCount(Long studyArticleId) {
        findStudyArticle(studyArticleId).plusViewCount();
        return HttpStatus.OK;
    }

    public StudyBoard findStudyBoard(int boardId) {
        return studyBoardRepository.findById(boardId)
                .orElseThrow(() -> new CustomException(ErrorCode.BOARD_NOT_FOUND));
    }

    public Member findMember(Long memberId) {
        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new CustomException(ErrorCode.MEMBER_NOT_FOUND));
        if (!member.getIs_active()) {
            throw new CustomException(ErrorCode.MEMBER_NOT_FOUND);
        }
        return member;
    }

    public StudyArticle findStudyArticle(Long studyArticleId) {
        return studyArticleRepository.findById(studyArticleId)
                .orElseThrow(() -> new CustomException(ErrorCode.ARTICLE_NOT_FOUND));
    }

    public StudyContent findStudyContent(StudyArticle studyArticle) {
        return studyContentRepository.getByStudyArticle(studyArticle)
                .orElseThrow(() -> new CustomException(ErrorCode.CONTENT_NOT_FOUND));
    }

    public void deleteAllCommentByStudyArticle(StudyArticle studyArticle) {
        studyArticleCommentRepository.deleteAllByStudyArticle(studyArticle);
    }

    // 게시글 태그 추가
    @Transactional
    public void addTags(StudyArticle studyArticle, List<String> tags) {
        studyArticleTagRepository.deleteAllByStudyArticle(studyArticle);
        if (tags == null) {
            return;
        }

        for (String tag : tags) {
            studyArticleTagRepository.save(StudyArticleTag.of(studyArticle, tag));
        }

    }

    // 가입 여부
    public void checkAuthority(Study study, Member member) {
        CompositeMemberStudy compositeMemberStudy = new CompositeMemberStudy(member, study);

        MemberStudy memberStudy = memberStudyRepository.findById(compositeMemberStudy)
                .orElseThrow(() -> new CustomException(ErrorCode.MEMBER_CLUB_NOT_FOUND));
        if (Boolean.FALSE.equals(memberStudy.getIsActive())) {
            throw new CustomException(ErrorCode.MEMBER_CLUB_NOT_FOUND);
        }

    }

    // 수정 권한 (가입 여부 + 작성자 확인)
    public void checkUpdateAuthority(Study study, Member member, StudyArticle studyArticle) {
        CompositeMemberStudy compositeMemberStudy = new CompositeMemberStudy(member, study);

        MemberStudy memberStudy = memberStudyRepository.findById(compositeMemberStudy)
                .orElseThrow(() -> new CustomException(ErrorCode.MEMBER_CLUB_NOT_FOUND));
        if (Boolean.FALSE.equals(memberStudy.getIsActive())) {
            throw new CustomException(ErrorCode.MEMBER_CLUB_NOT_FOUND);
        }

        if (!studyArticle.getMember().getId().equals(member.getId())) {
            throw new CustomException(ErrorCode.UNAUTHORIZED_CHANGE);
        }

    }

    // 삭제 권한 (가입 여부 + 작성자 + 관리자, 소유자)
    public void checkDeleteAuthority(Study study, Member member, StudyArticle studyArticle) {
        CompositeMemberStudy compositeMemberStudy = new CompositeMemberStudy(member, study);

        MemberStudy memberStudy = memberStudyRepository.findById(compositeMemberStudy)
                .orElseThrow(() -> new CustomException(ErrorCode.MEMBER_CLUB_NOT_FOUND));
        if (Boolean.FALSE.equals(memberStudy.getIsActive())) {
            throw new CustomException(ErrorCode.MEMBER_CLUB_NOT_FOUND);
        }

        if (!(memberStudy.getAuthority().equals(GroupAuthority.소유자) ||
                memberStudy.getAuthority().equals(GroupAuthority.관리자) ||
                studyArticle.getMember().getId().equals(member.getId()))) {
            throw new CustomException(ErrorCode.UNAUTHORIZED_CHANGE);
        }

    }

}