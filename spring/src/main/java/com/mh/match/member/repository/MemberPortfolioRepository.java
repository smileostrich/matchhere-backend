package com.mh.match.member.repository;

import com.mh.match.member.entity.MemberPortfolio;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MemberPortfolioRepository extends JpaRepository<MemberPortfolio, Integer> {

}