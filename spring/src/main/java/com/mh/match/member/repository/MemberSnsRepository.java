package com.mh.match.member.repository;

import com.mh.match.member.entity.Member;
import com.mh.match.member.entity.MemberSns;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface MemberSnsRepository extends JpaRepository<MemberSns, Integer> {
    Optional<MemberSns> findByMemberAndSnsName(Member member, String snsName);
    List<MemberSns> findAllByMember(Member member);

    void deleteAllByMember(Member member);
}