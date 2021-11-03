package com.mh.match.common.entity;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Getter
@NoArgsConstructor
//@Table
@Entity(name = "matching.refresh_token")
public class RefreshToken {
    @Id
    @Column(name = "member_id")
    private String key;

    private String value;

    public RefreshToken updateValue(String token) {
        this.value = token;
        return this;
    }

    @Builder
    public RefreshToken(String key, String value) {
        this.key = key;
        this.value = value;
    }
}