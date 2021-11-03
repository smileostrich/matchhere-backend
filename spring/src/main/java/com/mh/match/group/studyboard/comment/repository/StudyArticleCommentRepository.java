package com.mh.match.group.studyboard.comment.repository;

import com.mh.match.group.studyboard.article.entity.StudyArticle;
import com.mh.match.group.studyboard.comment.entity.StudyArticleComment;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.transaction.annotation.Transactional;

public interface StudyArticleCommentRepository extends JpaRepository<StudyArticleComment, Long> {

//    @Query("select sac from StudyArticleComment sac order by sac.parentId, sac.createDate")
//    List<StudyArticleComment> allComment(@Param("studyArticle") StudyArticle studyArticle);

    List<StudyArticleComment> findAllByStudyArticleOrderByParentIdDescCreateDateAsc(StudyArticle studyArticle);

    @Transactional
    @Modifying
    void deleteAllByStudyArticle(StudyArticle studyArticle);
}