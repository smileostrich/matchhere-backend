package com.mh.match.common.entity;

import com.mh.match.common.exception.CustomException;
import com.mh.match.common.exception.ErrorCode;

public enum Level {
    상("상"),
    중("중"),
    하("하");

    String value;
    Level(String value) { this.value = value; }
    public String value() { return value; }

    public static Level from(String s){
        Level level;
        if(s.equals("상")){
            level = Level.상;
        }else if(s.equals("중")){
            level = Level.중;
        }else if(s.equals("하")){
            level = Level.하;
        }else{
            throw new CustomException(ErrorCode.LEVEL_NOT_FOUND);
        }
        return level;
    }
}
