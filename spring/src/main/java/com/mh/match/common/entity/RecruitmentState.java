package com.mh.match.common.entity;

import com.mh.match.common.exception.CustomException;
import com.mh.match.common.exception.ErrorCode;

public enum RecruitmentState {
    RECRUITMENT("모집 중"), FINISH("모집 마감");

    private final String state;

    RecruitmentState(String state) {
        this.state = state;
    }

    public String getState(){
        return this.state;
    }

    public static RecruitmentState from(String s) {
        RecruitmentState recruitmentState;
        if(s.equals(RECRUITMENT.state)){
            recruitmentState = RECRUITMENT;
        }else if(s.equals(FINISH.state)){
            recruitmentState = FINISH;
        }else{
            throw new CustomException(ErrorCode.RECRUITMENT_STATE_NOT_FOUND);
        }
        return recruitmentState;
    }

}
