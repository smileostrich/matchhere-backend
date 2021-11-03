package com.mh.match.group.projectboard.article.repository;

import com.mh.match.group.projectboard.article.entity.ProjectArticle;
import com.mh.match.group.projectboard.article.entity.ProjectArticleTag;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;

public interface ProjectArticleTagRepository extends JpaRepository<ProjectArticleTag, Long> {

    // 현재 프로젝트의 태그 조회
    List<ProjectArticleTag> findAllByProjectArticle(@Param("projectArticle") ProjectArticle projectArticle);

    // 프로젝트 게시글의 기존 태그 전체 제거
//    @Transactional
//    @Modifying
//    @Query("delete from ProjectArticleTag pat where pat.projectArticle = :projectArticle")
//    void deleteAllTagsByProjectArticle(@Param("projectArticle") ProjectArticle projectArticle);

    void deleteAllByProjectArticle(ProjectArticle projectArticle);
}