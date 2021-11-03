package com.mh.match.group.clubboard.article.repository;


import com.mh.match.group.clubboard.article.entity.ClubArticle;
import com.mh.match.group.clubboard.board.entity.ClubBoard;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ClubArticleRepository extends JpaRepository<ClubArticle, Long> {
    Page<ClubArticle> findAllByClubBoard(ClubBoard clubBoard, Pageable pageable);

    List<ClubArticle> findAllByClubBoard(ClubBoard clubBoard);

//    // 제목으로 게시글 조회
//    @Query("select ca from matching.club_article ca "
//        + "where ca.clubBoard = :clubBoard "
//        + "and ca.title like %:title% order by ca.createDate desc")
//    Page<ClubArticle> findAllByClubBoardAndTitle(@Param("clubBoard") ClubBoard clubBoard, @Param("title") String title, Pageable pageable);

    // 제목으로 게시글 조회 (클래스 생성자를 이용한 방식도 있어서 그냥 해봤습니다)
//    @Query("select new com.ssafy.match.group.clubboard.article.dto.ClubArticleSimpleInfoResponseDto(ca.id, ca.title, ca.clubBoard.name, ca.member.nickname, ca.createDate, ca.modifiedDate, ca.viewCount)"
//        + " from matching.club_article ca where ca.clubBoard = :clubBoard "
//        + "and ca.title like %:title% order by ca.createDate desc")
//    Page<ClubArticleSimpleInfoResponseDto> findAllByClubBoardAndTitle(@Param("clubBoard") ClubBoard clubBoard, @Param("title") String title, Pageable pageable);

    // 닉네임으로 게시글 조회
//    @Query("select ca from matching.club_article ca "
//        + "where ca.clubBoard = :clubBoard "
//        + "and ca.member.nickname like %:nickname% order by ca.createDate desc")
//    Page<ClubArticle> findAllByClubBoardAndNickname(@Param("clubBoard") ClubBoard clubBoard, @Param("nickname") String nickname, Pageable pageable);
}
