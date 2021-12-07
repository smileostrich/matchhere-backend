package com.mh.match.portfolio.repository;

import java.util.List;

import com.mh.match.portfolio.entity.Portfolio;
import com.mh.match.portfolio.entity.PortfolioTag;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;

public interface PortfolioTagRepository extends JpaRepository<PortfolioTag, Long> {
    void deleteAllByPortfolio(Portfolio portfolio);

    List<PortfolioTag> findAllByPortfolio(@Param("portfolio") Portfolio portfolio);
}