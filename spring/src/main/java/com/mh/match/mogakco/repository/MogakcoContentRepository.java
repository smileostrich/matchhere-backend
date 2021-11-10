package com.mh.match.mogakco.repository;

import com.mh.match.mogakco.entity.MogakcoArticle;
import com.mh.match.mogakco.entity.MogakcoContent;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface MogakcoContentRepository extends JpaRepository<MogakcoContent, Long> {
    Optional<MogakcoContent> getByMogakcoArticle(MogakcoArticle mogakcoArticle);
}