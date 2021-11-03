package com.mh.match.member.entity;

import java.time.LocalDate;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

import lombok.*;

@Getter
@Setter
@Entity(name = "matching.career")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Career {
    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    private Long id;

    @NotEmpty
    private String company;

    @NotEmpty
    private String role;

    @NotNull
    private LocalDate start_date;
    private LocalDate end_date;
    private String description;

    @NotNull
    private Boolean is_incumbent;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Member member;

    @Builder
    public Career(String company, String role, LocalDate start_date,
        LocalDate end_date, String description, Boolean is_incumbent,
        Member member) {
        this.company = company;
        this.role = role;
        this.start_date = start_date;
        this.end_date = end_date;
        this.description = description;
        this.is_incumbent = is_incumbent;
        this.member = member;
    }
}