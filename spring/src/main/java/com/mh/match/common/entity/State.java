package com.mh.match.common.entity;

import com.fasterxml.jackson.annotation.JsonCreator;

public enum State {
    졸업, 재학, 중퇴, 휴학, 수료;

    @JsonCreator
    public static State from(String s) {
        return State.valueOf(s.toUpperCase());
    }
}