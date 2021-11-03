package com.mh.match.common.repository;

import com.mh.match.common.entity.Techstack;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface TechstackRepository extends JpaRepository<Techstack, Integer> {
    Optional<Techstack> findByName(String techName);

    @Query(value = "select t.name from matching.techstack t")
    List<String> findAllName();
}