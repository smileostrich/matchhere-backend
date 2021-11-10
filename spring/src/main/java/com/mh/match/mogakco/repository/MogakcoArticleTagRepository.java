package com.mh.match.mogakco.repository;

import com.mh.match.mogakco.entity.MogakcoArticle;
import com.mh.match.mogakco.entity.MogakcoArticleTag;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MogakcoArticleTagRepository extends JpaRepository<MogakcoArticleTag, Long> {

    // 현재 클럽의 태그 조회
    List<MogakcoArticleTag> findAllByMogakcoArticle(MogakcoArticle mogakcoArticle);

    void deleteAllByMogakcoArticle(MogakcoArticle mogakcoArticle);
}
