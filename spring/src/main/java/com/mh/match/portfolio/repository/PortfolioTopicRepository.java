package com.mh.match.portfolio.repository;

import java.util.List;

import com.mh.match.portfolio.entity.Portfolio;
import com.mh.match.portfolio.entity.PortfolioTopic;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;

public interface PortfolioTopicRepository extends JpaRepository<PortfolioTopic, Long> {
    void deleteAllByPortfolio(Portfolio portfolio);

    List<PortfolioTopic> findAllByPortfolio(@Param("portfolio") Portfolio portfolio);
}