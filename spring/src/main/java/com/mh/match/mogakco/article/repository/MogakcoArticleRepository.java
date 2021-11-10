package com.mh.match.mogakco.article.repository;


import com.mh.match.mogakco.article.entity.MogakcoArticle;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MogakcoArticleRepository extends JpaRepository<MogakcoArticle, Long> {
}
