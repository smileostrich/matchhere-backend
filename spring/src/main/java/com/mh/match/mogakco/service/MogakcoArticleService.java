package com.mh.match.mogakco.service;


import com.mh.match.common.exception.CustomException;
import com.mh.match.common.exception.ErrorCode;
import com.mh.match.member.entity.Member;
import com.mh.match.member.repository.MemberRepository;
import com.mh.match.mogakco.dto.MogakcoArticleInfoResponseDto;
import com.mh.match.mogakco.dto.MogakcoArticleRequestDto;
import com.mh.match.mogakco.dto.MogakcoArticleSimpleInfoResponseDto;
import com.mh.match.mogakco.entity.MogakcoArticle;
import com.mh.match.mogakco.entity.MogakcoArticleTag;
import com.mh.match.mogakco.entity.MogakcoContent;
import com.mh.match.mogakco.repository.MogakcoArticleRepository;
import com.mh.match.mogakco.repository.MogakcoArticleTagRepository;
import com.mh.match.mogakco.repository.MogakcoContentRepository;
import com.mh.match.util.SecurityUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class MogakcoArticleService {
    private final MogakcoArticleRepository mogakcoArticleRepository;
    private final MogakcoContentRepository mogakcoContentRepository;
    private final MemberRepository memberRepository;
    private final MogakcoArticleTagRepository mogakcoArticleTagRepository;
    private final MogakcoArticleCommentRepository mogakcoArticleCommentRepository;

    @Transactional(readOnly = true)
    public MogakcoArticleInfoResponseDto getMogakcoArticleDetail(Long articleId) {
        MogakcoArticle mogakcoArticle = findMogakcoArticle(articleId);

        MogakcoArticleInfoResponseDto mogakcoArticleInfoResponseDto = MogakcoArticleInfoResponseDto.of(mogakcoArticle, getMogakcoArticleTagList(mogakcoArticle));
        MogakcoContent mogakcoContent = findMogakcoContent(mogakcoArticle);
        mogakcoArticleInfoResponseDto.setContent(mogakcoContent.getContent());
        return mogakcoArticleInfoResponseDto;
    }

    public List<String> getMogakcoArticleTagList(MogakcoArticle mogakcoArticle) {
        List<MogakcoArticleTag> pats = mogakcoArticleTagRepository.findAllByMogakcoArticle(mogakcoArticle);
        List<String> tags = new ArrayList<>();
        for (MogakcoArticleTag pat : pats) {
            tags.add(pat.getName());
        }
        return tags;
    }

    @Transactional(readOnly = true)
    public Page<MogakcoArticleSimpleInfoResponseDto> getMogakcoArticles(Pageable pageable) {
        Page<MogakcoArticle> mogakcoArticles = mogakcoArticleRepository.findAll(pageable);
        return mogakcoArticles.map(m -> MogakcoArticleSimpleInfoResponseDto.of(m, getMogakcoArticleTagList(m)));
    }

    @Transactional
    public MogakcoArticleInfoResponseDto createArticle(MogakcoArticleRequestDto dto) {
        Member member = findMember(SecurityUtil.getCurrentMemberId());

        if (dto.getContent() == null) {
            throw new CustomException(ErrorCode.CONTENT_NOT_FOUND);
        }
        MogakcoArticle mogakcoArticle = mogakcoArticleRepository.save(MogakcoArticle.of(dto, member));

        addContent(mogakcoArticle, dto.getContent());
        addTags(mogakcoArticle, dto.getTags());

        MogakcoArticleInfoResponseDto mogakcoArticleInfoResponseDto = MogakcoArticleInfoResponseDto.of(mogakcoArticle, dto.getTags());
        mogakcoArticleInfoResponseDto.setContent(dto.getContent());
        return mogakcoArticleInfoResponseDto;
    }

    @Transactional
    public MogakcoArticleInfoResponseDto updateArticle(Long articleId, MogakcoArticleRequestDto dto) {
        MogakcoArticle mogakcoArticle = findMogakcoArticle(articleId);

        // 권한 체크 (소속원인지 확인)
        checkUpdateAuthority(findMember(SecurityUtil.getCurrentMemberId()), mogakcoArticle);

        if (dto.getContent() == null) {
            throw new CustomException(ErrorCode.CONTENT_NOT_FOUND);
        }

        // 게시글 내용
        MogakcoContent mogakcoContent = findMogakcoContent(mogakcoArticle);
        mogakcoContent.setContent(dto.getContent());

        mogakcoArticle.update(dto);
        addTags(mogakcoArticle, dto.getTags());

        MogakcoArticleInfoResponseDto mogakcoArticleInfoResponseDto = MogakcoArticleInfoResponseDto.of(mogakcoArticle, dto.getTags());
        mogakcoArticleInfoResponseDto.setContent(mogakcoContent.getContent());
        return mogakcoArticleInfoResponseDto;
    }

    @Transactional
    public HttpStatus deleteArticle(Long articleId) {
        MogakcoArticle mogakcoArticle = findMogakcoArticle(articleId);

        // 소속원이고 작성자이거나 소유자, 관리자인지
        checkUpdateAuthority(findMember(SecurityUtil.getCurrentMemberId()), mogakcoArticle);

        MogakcoContent mogakcoContent = findMogakcoContent(mogakcoArticle);
        mogakcoArticleCommentRepository.deleteAllByMogakcoArticle(mogakcoArticle);
        mogakcoArticleTagRepository.deleteAllByMogakcoArticle(mogakcoArticle);
        mogakcoContentRepository.delete(mogakcoContent);
        mogakcoArticleRepository.delete(mogakcoArticle);
        return HttpStatus.OK;
    }

    @Transactional
    public void addContent(MogakcoArticle mogakcoArticle, String content) {
        mogakcoContentRepository.save(MogakcoContent.of(mogakcoArticle, content));
    }

    public Member findMember(Long memberId) {
        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new CustomException(ErrorCode.MEMBER_NOT_FOUND));
        if (!member.getIs_active()) {
            throw new CustomException(ErrorCode.MEMBER_NOT_FOUND);
        }
        return member;
    }

    public MogakcoArticle findMogakcoArticle(Long mogakcoArticleId) {
        return mogakcoArticleRepository.findById(mogakcoArticleId)
                .orElseThrow(() -> new CustomException(ErrorCode.ARTICLE_NOT_FOUND));
    }

    public MogakcoContent findMogakcoContent(MogakcoArticle mogakcoArticle) {
        return mogakcoContentRepository.getByMogakcoArticle(mogakcoArticle)
                .orElseThrow(() -> new CustomException(ErrorCode.CONTENT_NOT_FOUND));
    }

    // 게시글 태그 추가
    @Transactional
    public void addTags(MogakcoArticle mogakcoArticle, List<String> tags) {
        mogakcoArticleTagRepository.deleteAllByMogakcoArticle(mogakcoArticle);
        if (tags == null) {
            return;
        }
        for (String tag : tags) {
            mogakcoArticleTagRepository.save(MogakcoArticleTag.of(mogakcoArticle, tag));
        }

    }

    // 수정 권한 (가입 여부 + 작성자 확인)
    public void checkUpdateAuthority(Member member, MogakcoArticle mogakcoArticle) {
        if (!mogakcoArticle.getMember().getId().equals(member.getId())) {
            throw new CustomException(ErrorCode.UNAUTHORIZED_CHANGE);
        }
    }

}