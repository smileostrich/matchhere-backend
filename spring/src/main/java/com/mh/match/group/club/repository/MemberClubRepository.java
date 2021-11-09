package com.mh.match.group.club.repository;

import com.mh.match.member.entity.Member;
import com.mh.match.group.club.entity.Club;
import com.mh.match.group.club.entity.CompositeMemberClub;
import com.mh.match.group.club.entity.MemberClub;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface MemberClubRepository extends JpaRepository<MemberClub, CompositeMemberClub> {

    // 특정 클럽에 속한 멤버의 관계 정보
    @Query(value = "select mc from matching.member_club mc "
        + "where mc.compositeMemberClub.club = :club and mc.isActive = true")
    List<MemberClub> findMemberRelationInClub(@Param("club") Club club);

    // 특정 클럽에 속한 멤버 정보
    @Query(value = "select mc.compositeMemberClub.member from matching.member_club mc "
        + "where mc.compositeMemberClub.club = :club and mc.isActive = true")
    List<Member> findMemberInClub(@Param("club") Club club);

    // 해당 멤버가 속한 클럽 리스트
    @Query(value = "select mc.compositeMemberClub.club from matching.member_club mc "
        + "where mc.compositeMemberClub.member = :member and mc.isActive = true")
    List<Club> findClubByMember(@Param("member") Member member);

    @Query(value = "select mc.compositeMemberClub.member from matching.member_club mc "
            + "where mc.compositeMemberClub.club.id = :id and mc.isActive = true")
    List<Member> findMemberByClubId(@Param("id") Long id);

    void deleteAllByCompositeMemberClub_Member(Member member);
}