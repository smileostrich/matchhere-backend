package com.mh.match.member.repository;

import com.mh.match.member.dto.inter.CareerInterface;
import com.mh.match.member.entity.Career;
import com.mh.match.member.entity.Member;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CareerRepository extends JpaRepository<Career, Long> {
    @Query(value = "select mc.id as id, mc.company as company, mc.role as role, mc.start_date as start_date, mc.end_date as end_date, mc.is_incumbent as is_incumbent, mc.description as description from matching.career mc where mc.member = :member")
    List<CareerInterface> findAllByMember(@Param("member") Member member);

    void deleteAllByMember(Member member);

    Optional<Career> findByMemberAndId(Member member, Long id);
}