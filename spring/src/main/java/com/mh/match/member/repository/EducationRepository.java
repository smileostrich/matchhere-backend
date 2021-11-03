package com.mh.match.member.repository;

import com.mh.match.member.dto.inter.EducationInterface;
import com.mh.match.member.entity.Education;
import com.mh.match.member.entity.Member;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface EducationRepository extends JpaRepository<Education, Long> {
    @Query(value = "select me.id as id, me.institution as institution, me.degree as degree, me.major as major, me.start_date as start_date, me.end_date as end_date, me.state as state, me.description as description from matching.education me where me.member = :member")
    List<EducationInterface> findAllByMember(Member member);

    Optional<Education> findByMemberAndId(Member member, Long id);
}