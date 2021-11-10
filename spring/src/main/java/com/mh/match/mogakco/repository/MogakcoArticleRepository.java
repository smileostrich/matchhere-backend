package com.mh.match.mogakco.repository;


import com.mh.match.mogakco.entity.MogakcoArticle;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MogakcoArticleRepository extends JpaRepository<MogakcoArticle, Long> {
}
