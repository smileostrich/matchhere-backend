package com.mh.match.member.entity;

import com.mh.match.s3.entity.DBFile;
import com.mh.match.common.entity.Authority;
import java.time.LocalDateTime;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.validation.constraints.NotEmpty;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@Entity(name = "matching.member")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Member {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotEmpty
    private String email;
    @NotEmpty
    private String name;
    @NotEmpty
    private String password;
    @NotEmpty
    private String nickname;
    private LocalDateTime create_date;
    private String tel;
    private String bio;
    private String city;
    private Boolean banned;
    private String position;
    private Boolean is_active;
    private String portfolio_uri;
    private Boolean is_agree;
    private Boolean is_pagree;

    @Enumerated(EnumType.STRING)
    private Authority authority;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "cover_pic")
    private DBFile cover_pic;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "portfolio_uuid")
    private DBFile portfolio;

    @Builder
    public Member(LocalDateTime create_date, String email, String name, String password,
                  String nickname, String tel, String bio, String city, Boolean banned, String position,
                  Boolean is_active, Authority authority, DBFile cover_pic, DBFile portfolio, String portfolio_uri, Boolean is_agree, Boolean is_pagree) {
        this.create_date = create_date;
        this.email = email;
        this.name = name;
        this.password = password;
        this.nickname = nickname;
        this.tel = tel;
        this.bio = bio;
        this.city = city;
        this.banned = banned;
        this.position = position;
        this.is_active = is_active;
        this.authority = authority;
        this.cover_pic = cover_pic;
        this.portfolio = portfolio;
        this.portfolio_uri = portfolio_uri;
        this.is_agree = is_agree;
        this.is_pagree = is_pagree;
    }
}