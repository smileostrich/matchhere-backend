package com.mh.match.member.entity;


import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

@Getter
@Entity(name = "matching.emailcheck")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class EmailCheck {
    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    private Long id;

    private String email;

    @Column(name="auth_code")
    private String authCode;

    private Boolean is_check;

    public EmailCheck(String email, String authCode, Boolean is_check) {
        this.email = email;
        this.authCode = authCode;
        this.is_check = is_check;
    }

    public void updateKey(String key) {
        this.authCode = key;
    }

    public void updateIsCheck(Boolean sig) {
        this.is_check = sig;
    }
}
