package com.mh.match.group.studyboard.board.repository;


import com.mh.match.group.studyboard.board.entity.StudyBoard;
import com.mh.match.group.study.entity.Study;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface StudyBoardRepository extends JpaRepository<StudyBoard, Integer> {
    List<StudyBoard> findAllByStudy(Study study);
}
