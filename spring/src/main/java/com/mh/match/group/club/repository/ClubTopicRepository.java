package com.mh.match.group.club.repository;

import com.mh.match.group.club.entity.ClubTopic;
import com.mh.match.group.club.entity.Club;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;

public interface ClubTopicRepository extends JpaRepository<ClubTopic, Long> {

    // 클럽의 기존 주제 전체 제거
//    @Transactional
//    @Modifying
//    @Query("delete from ClubTopic ct where ct.club = :club")
//    void deleteAllByClub(@Param("club") Club club);

    void deleteAllByClub(Club club);

    List<ClubTopic> findAllByClub(@Param("club") Club club);
}