package com.mh.match.member.repository;

import com.mh.match.member.entity.Member;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface MemberRepository extends JpaRepository<Member, Long> {
    Optional<Member> findByEmail(String email);
    boolean existsByEmail(String email);
    boolean existsByNickname(String nickname);
    boolean existsMemberByIdAndPassword(Long id, String password);

//    @Query("select new com.ssafy.match.controller.dto.MemberInfoDto(m.email, m.name, m.nickname, m.tel, m.bio, m.city, m.position, m.dbFile, m.myClubList) from Member m")
//    List<MemberInfoDto> findMemberInfoDto();
}