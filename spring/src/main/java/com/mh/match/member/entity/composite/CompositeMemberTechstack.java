package com.mh.match.member.entity.composite;

import com.mh.match.common.entity.Techstack;
import com.mh.match.member.entity.Member;
import java.io.Serializable;
import javax.persistence.Embeddable;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

import lombok.*;

@Getter
@Setter
@Embeddable
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class CompositeMemberTechstack implements Serializable {

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "techstack_id")
    private Techstack techstack;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Member member;

    @Builder
    public CompositeMemberTechstack(Techstack techstack, Member member) {
        this.techstack = techstack;
        this.member = member;
    }
}