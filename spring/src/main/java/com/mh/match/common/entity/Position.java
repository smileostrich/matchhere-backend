package com.mh.match.common.entity;

public enum Position {
    개발자("개발자"),
    기획자("기획자"),
    디자이너("디자이너");

    String value;
    Position(String value) { this.value = value; }
    public String value() { return value; }
}
