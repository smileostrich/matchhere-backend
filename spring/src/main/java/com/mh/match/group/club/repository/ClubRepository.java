package com.mh.match.group.club.repository;

import com.mh.match.common.entity.PublicScope;
import com.mh.match.common.entity.RecruitmentState;
import com.mh.match.group.club.entity.Club;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface ClubRepository extends JpaRepository<Club, Long> {

    @Query("select c from matching.club c "
        + "where c.recruitmentState = :recruitmentState and c.publicScope = :publicScope and c.isActive = true")
    Page<Club> findAllClub(
        @Param("recruitmentState") RecruitmentState recruitmentState,
        @Param("publicScope") PublicScope publicScope,
        Pageable pageable);

}