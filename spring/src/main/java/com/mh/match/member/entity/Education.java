package com.mh.match.member.entity;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

import lombok.*;

@Getter
@Setter
@Entity(name = "matching.education")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Education {

    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    private Long id;

    private String institution;
    private String degree;
    private String major;
    private Integer start_date;
    private Integer end_date;
    private String state;
    private String description;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Member member;

    @Builder
    public Education(String institution, String degree, String major,
        Integer start_date, Integer end_date, String state, String description,
        Member member) {
        this.institution = institution;
        this.degree = degree;
        this.major = major;
        this.start_date = start_date;
        this.end_date = end_date;
        this.state = state;
        this.description = description;
        this.member = member;
    }
}