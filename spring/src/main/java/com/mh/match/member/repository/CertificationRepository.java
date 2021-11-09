package com.mh.match.member.repository;

import com.mh.match.member.dto.inter.CertificationInterface;
import com.mh.match.member.entity.Certification;
import com.mh.match.member.entity.Member;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CertificationRepository extends JpaRepository<Certification, Long> {
    @Query(value = "select mc.id as id, mc.name as name, mc.organization as organization, mc.code as code, mc.grade as grade, mc.issued_date as issued_date, mc.expired_date as expired_date, mc.is_expire as is_expire from matching.certification mc where mc.member = :member")
    List<CertificationInterface> findAllByMember(Member member);

    Optional<Certification> findByMemberAndId(Member member, Long id);

    void deleteAllByMember(Member member);
}