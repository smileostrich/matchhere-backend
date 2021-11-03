package com.mh.match.group.study.repository;

import com.mh.match.group.study.entity.Study;
import com.mh.match.group.study.entity.StudyTopic;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;

public interface StudyTopicRepository extends JpaRepository<StudyTopic, Long> {
    // 스터디의 기존 주제 전체 제거
//    @Transactional
//    @Modifying
//    @Query("delete from StudyTopic st where st.study = :study")
//    void deleteAllByStudy(@Param("study") Study study);

    void deleteAllByStudy(Study study);

    List<StudyTopic> findAllByStudy(@Param("study") Study study);
}