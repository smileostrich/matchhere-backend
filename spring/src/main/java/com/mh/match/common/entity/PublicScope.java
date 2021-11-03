package com.mh.match.common.entity;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.mh.match.common.exception.CustomException;
import com.mh.match.common.exception.ErrorCode;

public enum PublicScope {
    PUBLIC("전체 공개"), ClUBONLY("클럽 멤버에게만 공개"), PRIVATE("비공개");

    private final String state;

    PublicScope(String state) {
        this.state = state;
    }

    public String getState() {
        return this.state;
    }

    @JsonCreator
    public static PublicScope from(String s) {
        PublicScope publicScope;
        if (s.equals(PUBLIC.state)) {
            publicScope = PUBLIC;
        } else if (s.equals(ClUBONLY.state)) {
            publicScope = ClUBONLY;
        } else if (s.equals(PRIVATE.state)) {
            publicScope = PRIVATE;
        } else {
            throw new CustomException(ErrorCode.PUBLIC_SCOPE_NOT_FOUND);
        }
        return publicScope;
    }

}
