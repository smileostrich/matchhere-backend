package com.mh.match.member.repository;

import com.mh.match.common.entity.Techstack;
import com.mh.match.member.dto.inter.MemberTechstackInterface;
import com.mh.match.member.entity.Member;
import com.mh.match.member.entity.MemberTechstack;
import com.mh.match.member.entity.composite.CompositeMemberTechstack;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface MemberTechstackRepository extends JpaRepository<MemberTechstack, CompositeMemberTechstack> {

    @Query(value = "select mt.compositeMemberTechstack.techstack.name as name, mt.compositeMemberTechstack.techstack.img_uri as img_uri, mt.level as level from matching.member_techstack mt where mt.compositeMemberTechstack.member = :member")
    List<MemberTechstackInterface> findTechstackByMember(@Param("member") Member member);

    Optional<MemberTechstack> findByCompositeMemberTechstack_MemberAndCompositeMemberTechstack_Techstack(Member member, Techstack techstack);

    List<MemberTechstack> findAllByCompositeMemberTechstack_Member(Member member);

    void deleteAllByCompositeMemberTechstack_Member(Member member);
}