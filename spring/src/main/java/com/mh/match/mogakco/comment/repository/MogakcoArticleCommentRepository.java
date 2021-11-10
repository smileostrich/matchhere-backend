package com.mh.match.mogakco.comment.repository;

import com.mh.match.mogakco.article.entity.MogakcoArticle;
import com.mh.match.mogakco.comment.entity.MogakcoArticleComment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Repository
public interface MogakcoArticleCommentRepository extends JpaRepository<MogakcoArticleComment, Long> {

    List<MogakcoArticleComment> findAllByMogakcoArticleOrderByParentIdDescCreateDateAsc(MogakcoArticle mogakcoArticle);

    @Transactional
    @Modifying
    void deleteAllByMogakcoArticle(MogakcoArticle mogakcoArticle);
}