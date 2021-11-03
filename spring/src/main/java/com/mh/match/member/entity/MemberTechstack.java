package com.mh.match.member.entity;

import com.mh.match.member.entity.composite.CompositeMemberTechstack;

import javax.persistence.EmbeddedId;
import javax.persistence.Entity;

import lombok.*;

@Getter
//@Setter
@Entity(name = "matching.member_techstack")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class MemberTechstack {

    @EmbeddedId
    private CompositeMemberTechstack compositeMemberTechstack;

    private String level;

    @Builder
    public MemberTechstack(
        CompositeMemberTechstack compositeMemberTechstack, String level) {
        this.compositeMemberTechstack = compositeMemberTechstack;
        this.level = level;
    }
}