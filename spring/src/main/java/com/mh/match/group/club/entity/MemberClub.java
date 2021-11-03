package com.mh.match.group.club.entity;

import com.mh.match.common.entity.GroupAuthority;

import java.time.LocalDateTime;
import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@Builder
@Entity(name = "matching.member_club")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class MemberClub {

    @EmbeddedId
    private CompositeMemberClub compositeMemberClub;

    @Column(name = "register_date")
    private LocalDateTime registerDate;

    @Enumerated(EnumType.STRING)
    private GroupAuthority authority;

    @Column(name = "is_active")
    private Boolean isActive;

    public void activation() {
        this.isActive = true;
    }

    public void deActivation() {
        this.isActive = false;
    }

}