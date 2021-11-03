package com.mh.match.member.entity;

import javax.persistence.*;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;

@Getter
@Setter
@Entity(name = "matching.member_sns")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class MemberSns {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @JsonIgnore
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Member member;

    @Column(name = "sns_name")
    private String snsName;

    @Column(name = "sns_account")
    private String snsAccount;

    @Builder
    public MemberSns(Member member, String snsName, String snsAccount) {
        this.member = member;
        this.snsName = snsName;
        this.snsAccount = snsAccount;
    }
}