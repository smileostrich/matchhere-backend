package com.mh.match.common.entity;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.mh.match.member.entity.Member;
import lombok.*;

@Getter
@Setter
@Entity(name = "matching.detail_position")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class DetailPosition {
    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    private int id;

    @JsonIgnore
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Member member;

    private String name;

    @Builder
    public DetailPosition(Member member, String name) {
        this.member = member;
        this.name = name;
    }
}