package com.mh.match.member.entity;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@Entity(name = "matching.member_portfolio")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class MemberPortfolio {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Member member;

    private String protfolio_uuid;

    public MemberPortfolio(Member member, String protfolio_uuid) {
        this.member = member;
        this.protfolio_uuid = protfolio_uuid;
    }
}