package com.mh.match.member.repository;


import com.mh.match.member.entity.EmailCheck;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface EmailCheckRepository extends JpaRepository<EmailCheck, Long> {
    Optional<EmailCheck> findByEmail(String email);

    boolean existsByEmail(String email);
}
