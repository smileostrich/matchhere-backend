package com.mh.match.common.entity;

import com.mh.match.common.exception.ErrorCode;
import com.mh.match.common.exception.CustomException;

public enum ProjectProgressState {
    READY("프로젝트 준비 중"), PROGRESS("프로젝트 진행 중"), FINISH("프로젝트 종료");

    private final String state;

    ProjectProgressState(String state) {
        this.state = state;
    }

    public String getState(){
        return this.state;
    }

    public static ProjectProgressState from(String s) {
        ProjectProgressState projectProgressState;
        if(s.equals(READY.state)){
            projectProgressState = READY;
        }else if(s.equals(PROGRESS.state)){
            projectProgressState = PROGRESS;
        }else if(s.equals(FINISH.state)){
            projectProgressState = FINISH;
        }else{
            throw new CustomException(ErrorCode.PROJECT_PROGRESS_STATE_NOT_FOUND);
        }
        return projectProgressState;
    }

}

