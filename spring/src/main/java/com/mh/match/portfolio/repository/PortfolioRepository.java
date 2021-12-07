package com.mh.match.portfolio.repository;

import com.mh.match.group.club.entity.Club;
import com.mh.match.portfolio.entity.Portfolio;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface PortfolioRepository extends JpaRepository<Portfolio, Long> {

    @Query("select p from matching.portfolio p")
    Page<Portfolio> findAllPortfolio(Pageable pageable);
}