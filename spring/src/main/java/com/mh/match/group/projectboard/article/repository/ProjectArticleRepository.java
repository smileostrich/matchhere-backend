package com.mh.match.group.projectboard.article.repository;


import com.mh.match.group.projectboard.article.entity.ProjectArticle;
import com.mh.match.group.projectboard.board.entity.ProjectBoard;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface ProjectArticleRepository extends JpaRepository<ProjectArticle, Long> {
    Page<ProjectArticle> findAllByProjectBoard(ProjectBoard projectBoard, Pageable pageable);

    List<ProjectArticle> findAllByProjectBoard(ProjectBoard projectBoard);

    // 제목으로 게시글 조회
    @Query("select pa from matching.project_article pa "
        + "where pa.projectBoard = :projectBoard "
        + "and pa.title like %:title% order by pa.createDate desc")
    Page<ProjectArticle> findAllByProjectBoardAndTitle(@Param("projectBoard") ProjectBoard projectBoard, @Param("title") String title, Pageable pageable);

    // 닉네임으로 게시글 조회
    @Query("select pa from matching.project_article pa "
        + "where pa.projectBoard = :projectBoard "
        + "and pa.member.nickname like %:nickname% order by pa.createDate desc")
    Page<ProjectArticle> findAllByProjectBoardAndNickname(@Param("projectBoard") ProjectBoard projectBoard, @Param("nickname") String nickname, Pageable pageable);
}
