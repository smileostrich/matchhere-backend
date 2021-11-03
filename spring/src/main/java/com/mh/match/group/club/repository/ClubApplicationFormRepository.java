package com.mh.match.group.club.repository;

import com.mh.match.group.club.entity.Club;
import com.mh.match.group.club.entity.ClubApplicationForm;
import com.mh.match.group.club.entity.CompositeMemberClub;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface ClubApplicationFormRepository extends
    JpaRepository<ClubApplicationForm, CompositeMemberClub> {

    // 모든 신청서 날짜 내림차순 조회
//    @Query("select c from ClubApplicationForm c where c.compositeMemberClub.club = :club order by c.createDate desc")
//    List<ClubApplicationForm> formByClubId(@Param("club") Club club);

    List<ClubApplicationForm> findAllByCompositeMemberClub_ClubOrderByCreateDateDesc(Club club);

    // 닉네임으로 신청서 조회
//    @Query("select p from ClubApplicationForm p "
//        + "where p.compositeMemberClub.club = :club "
//        + "and p.nickname like %:nickname% order by p.createDate desc")
//    List<ClubApplicationForm> allFormByClubNickname(@Param("club") Club club, @Param("nickname") String nickname);

    // 신청서 조회
    @Query("select c from ClubApplicationForm c where c.compositeMemberClub = :cmc")
    Optional<ClubApplicationForm> oneFormById(@Param("cmc") CompositeMemberClub cmc);

}