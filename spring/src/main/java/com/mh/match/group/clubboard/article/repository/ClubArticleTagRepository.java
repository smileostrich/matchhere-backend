package com.mh.match.group.clubboard.article.repository;

import com.mh.match.group.clubboard.article.entity.ClubArticle;
import com.mh.match.group.clubboard.article.entity.ClubArticleTag;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ClubArticleTagRepository extends JpaRepository<ClubArticleTag, Long> {

    // 현재 클럽의 태그 조회
    List<ClubArticleTag> findAllByClubArticle(ClubArticle clubArticle);

    // 클럽 게시글의 기존 태그 전체 제거
//    @Transactional
//    @Modifying
//    @Query("delete from matching.club_article_tag pat where pat.clubArticle = :clubArticle")
//    void deleteAllTagsByClubArticle(@Param("clubArticle") ClubArticle clubArticle);

    void deleteAllByClubArticle(ClubArticle clubArticle);
}
