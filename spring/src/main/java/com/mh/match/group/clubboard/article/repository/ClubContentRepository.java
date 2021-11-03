package com.mh.match.group.clubboard.article.repository;


import com.mh.match.group.clubboard.article.entity.ClubArticle;
import com.mh.match.group.clubboard.article.entity.ClubContent;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ClubContentRepository extends JpaRepository<ClubContent, Long> {
    Optional<ClubContent> getByClubArticle(ClubArticle clubArticle);
}