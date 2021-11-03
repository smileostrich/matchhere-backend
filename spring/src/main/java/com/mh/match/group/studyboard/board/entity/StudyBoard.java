package com.mh.match.group.studyboard.board.entity;


import com.mh.match.group.study.entity.Study;
import com.mh.match.group.studyboard.board.dto.StudyBoardCreateRequestDto;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.validation.constraints.NotEmpty;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@Entity(name = "matching.study_board")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class StudyBoard {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "study_id")
    private Study study;

    @NotEmpty
    @Column(name = "name")
    private String name;

    public void setName(String name) {
        this.name = name;
    }

    public static StudyBoard of(StudyBoardCreateRequestDto dto, Study study) {
        return StudyBoard.builder()
            .study(study)
            .name(dto.getName())
            .build();
    }

    public StudyBoard(String name, Study study) {
        this.study = study;
        this.name = name;
    }
}
