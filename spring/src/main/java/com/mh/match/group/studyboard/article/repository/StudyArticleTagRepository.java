package com.mh.match.group.studyboard.article.repository;

import com.mh.match.group.studyboard.article.entity.StudyArticle;
import com.mh.match.group.studyboard.article.entity.StudyArticleTag;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface StudyArticleTagRepository extends JpaRepository<StudyArticleTag, Long> {

    // 현재 스터디의 태그 조회
    List<StudyArticleTag> findAllByStudyArticle(StudyArticle studyArticle);

    // 스터디 게시글의 기존 태그 전체 제거
//    @Transactional
//    @Modifying
//    @Query("delete from StudyArticleTag pat where pat.studyArticle = :studyArticle")
//    void deleteAllTagsByStudyArticle(@Param("studyArticle") StudyArticle studyArticle);

    void deleteAllByStudyArticle(StudyArticle studyArticle);
}