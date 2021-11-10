package com.mh.match.mogakco.article.repository;

import com.mh.match.mogakco.article.entity.MogakcoArticle;
import com.mh.match.mogakco.article.entity.MogakcoArticleTag;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MogakcoArticleTagRepository extends JpaRepository<MogakcoArticleTag, Long> {

    // 현재 클럽의 태그 조회
    List<MogakcoArticleTag> findAllByMogakcoArticle(MogakcoArticle mogakcoArticle);

    void deleteAllByMogakcoArticle(MogakcoArticle mogakcoArticle);
}
