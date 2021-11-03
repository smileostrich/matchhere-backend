package com.mh.match.group.clubboard.comment.repository;

import com.mh.match.group.clubboard.article.entity.ClubArticle;
import com.mh.match.group.clubboard.comment.entity.ClubArticleComment;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
public interface ClubArticleCommentRepository extends JpaRepository<ClubArticleComment, Long> {

//    @Query("select sac from ClubArticleComment sac order by sac.parentId, sac.createDate")
//    List<ClubArticleComment> allComment(@Param("clubArticle") ClubArticle clubArticle);

    List<ClubArticleComment> findAllByClubArticleOrderByParentIdDescCreateDateAsc(ClubArticle clubArticle);

    @Transactional
    @Modifying
    void deleteAllByClubArticle(ClubArticle clubArticle);
}