package com.mh.match.common.entity;

import com.fasterxml.jackson.annotation.JsonCreator;

public enum Platform {
    googlemeet("Google Meet"),
    discord("Discord"),
    zoom("Zoom"),
    hangout("Hangout");

    String value;
    Platform(String value) { this.value = value; }
    public String value() { return value; }

    @JsonCreator
    public static Platform from(String s) {
        return Platform.valueOf(s.toUpperCase());
    }
}
