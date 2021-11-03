package com.mh.match.common.entity;

import com.mh.match.common.exception.CustomException;
import com.mh.match.common.exception.ErrorCode;

public enum StudyProgressState {
    READY("스터디 준비 중"), PROGRESS("스터디 진행 중"), FINISH("스터디 종료");

    private final String state;

    StudyProgressState(String state) {
        this.state = state;
    }

    public String getState(){
        return this.state;
    }

    public static StudyProgressState from(String s) {
        StudyProgressState projectProgressState;
        if(s.equals(READY.state)){
            projectProgressState = READY;
        }else if(s.equals(PROGRESS.state)){
            projectProgressState = PROGRESS;
        }else if(s.equals(FINISH.state)){
            projectProgressState = FINISH;
        }else {
            throw new CustomException(ErrorCode.STUDY_PROGRESS_STATE_NOT_FOUND);
        }
        return projectProgressState;
    }

}

