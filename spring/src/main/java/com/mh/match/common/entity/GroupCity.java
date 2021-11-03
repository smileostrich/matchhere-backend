package com.mh.match.common.entity;

import com.fasterxml.jackson.annotation.JsonCreator;

public enum GroupCity {
    서울("서울"),
    부산("부산"),
    대구("대구"),
    인천("인천"),
    광주("광주"),
    대전("대전"),
    울산("울산"),
    세종("세종"),
    경기("경기"),
    강원("강원"),
    충북("충북"),
    충남("충남"),
    전북("전북"),
    전남("전남"),
    경북("경북"),
    경남("경남"),
    제주("제주"),
    온라인("온라인"),
    무관("무관");

    String value;
    GroupCity(String value) { this.value = value; }
    public String value() { return value; }

    @JsonCreator
    public static GroupCity from(String s) {
        return GroupCity.valueOf(s.toUpperCase());
    }
}
