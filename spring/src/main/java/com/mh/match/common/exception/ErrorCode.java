package com.mh.match.common.exception;

import static org.springframework.http.HttpStatus.BAD_REQUEST;
import static org.springframework.http.HttpStatus.CONFLICT;
import static org.springframework.http.HttpStatus.NOT_FOUND;
import static org.springframework.http.HttpStatus.UNAUTHORIZED;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public enum ErrorCode {

    /* 400 BAD_REQUEST : 정의된 로직의 조건을 위반하는 경우 */
    INVALID_REFRESH_TOKEN(BAD_REQUEST, "리프레시 토큰이 유효하지 않습니다"),
    MISMATCH_REFRESH_TOKEN(BAD_REQUEST, "리프레시 토큰의 유저 정보가 일치하지 않습니다"),
    DEVELOPER_COUNT_OVER(BAD_REQUEST, "개발자 인원 한계를 초과했습니다."),
    PLANNER_COUNT_OVER(BAD_REQUEST, "기획자 인원 한계를 초과했습니다."),
    DESIGNER_COUNT_OVER(BAD_REQUEST, "디자이너 인원 한계를 초과했습니다."),
    MEMBER_COUNT_OVER(BAD_REQUEST, "인원 한계를 초과했습니다."),
    DEVELOPER_COUNT_BELOW_ZERO(BAD_REQUEST, "개발자 인원은 0 아래가 될 수 없습니다."),
    PLANNER_COUNT_BELOW_ZERO(BAD_REQUEST, "기획자 인원은 0 아래가 될 수 없습니다."),
    DESIGNER_COUNT_BELOW_ZERO(BAD_REQUEST, "디자이너은 0 아래가 될 수 없습니다."),
    MEMBER_COUNT_BELOW_ZERO(BAD_REQUEST, "인원은 0 아래가 될 수 없습니다."),
    DEVELOPER_COUNT_MORE_THAN_MAX(BAD_REQUEST, "개발자 최대 인원이 현재 인원보다 작습니다."),
    PLANNER_COUNT_MORE_THAN_MAX(BAD_REQUEST, "기획자 최대 인원이 현재 인원보다 작습니다."),
    DESIGNER_COUNT_MORE_THAN_MAX(BAD_REQUEST, "디자이너 최대 인원이 현재 인원보다 작습니다."),
    HOST_CANNOT_LEAVE(BAD_REQUEST, "소유자는 탈퇴할 수 없습니다."),
    COMMON_MEMBER_CANNOT_REMOVE(BAD_REQUEST, "팀원은 강퇴 기능을 가지고 있지 않습니다."),
    ONLY_CAN_REMOVE_COMMON(BAD_REQUEST, "팀원만 퇴장 시킬 수 있습니다."),
    CANNOT_APPLY(BAD_REQUEST, "신청할 수 없습니다."),
    ALREADY_JOIN(BAD_REQUEST, "이미 가입되어있습니다."),
    ALREADY_APPLY(BAD_REQUEST, "이미 신청되어있습니다."),

    /* 401 UNAUTHORIZED : 인증되지 않은 사용자 */
    INVALID_AUTH_TOKEN(UNAUTHORIZED, "권한 정보가 없는 토큰입니다"),
    UNAUTHORIZED_MEMBER(UNAUTHORIZED, "현재 내 계정 정보가 존재하지 않습니다"),
    UNAUTHORIZED_CHANGE(UNAUTHORIZED, "변경 권한을 가지고 있지 않습니다."),
    UNAUTHORIZED_SELECT(UNAUTHORIZED, "조회 권한을 가지고 있지 않습니다."),


    /* 404 NOT_FOUND : Resource 를 찾을 수 없음 */
    MEMBER_NOT_FOUND(NOT_FOUND, "해당 유저 정보를 찾을 수 없습니다"),
    MEMBER_PROJECT_NOT_FOUND(NOT_FOUND, "해당 프로젝트의 유저 정보를 찾을 수 없습니다"),
    MEMBER_STUDY_NOT_FOUND(NOT_FOUND, "해당 스터디의 유저 정보를 찾을 수 없습니다"),
    REFRESH_TOKEN_NOT_FOUND(NOT_FOUND, "로그아웃 된 사용자입니다"),
    PROJECT_NOT_FOUND(NOT_FOUND, "해당 프로젝트 정보를 찾을 수 없습니다"),
    STUDY_NOT_FOUND(NOT_FOUND, "해당 스터디 정보를 찾을 수 없습니다"),
    CLUB_NOT_FOUND(NOT_FOUND, "해당 클럽 정보를 찾을 수 없습니다"),
    FILE_NOT_FOUND(NOT_FOUND, "해당 파일 정보를 찾을 수 없습니다"),
    DELETED_PROJECT(NOT_FOUND, "제거된 프로젝트입니다."),
    DELETED_STUDY(NOT_FOUND, "제거된 스터디입니다."),
    DELETED_CLUB(NOT_FOUND, "제거된 클럽입니다."),
    CITY_NOT_FOUND(NOT_FOUND, "해당 지역 정보를 찾을 수 없습니다."),
    PROJECT_PROGRESS_STATE_NOT_FOUND(NOT_FOUND, "해당 프로젝트 진행 상태를 찾을 수 없습니다."),
    STUDY_PROGRESS_STATE_NOT_FOUND(NOT_FOUND, "해당 스터디 진행 상태를 찾을 수 없습니다."),
    RECRUITMENT_STATE_NOT_FOUND(NOT_FOUND, "해당 모집 상태를 찾을 수 없습니다."),
    PUBLIC_SCOPE_NOT_FOUND(NOT_FOUND, "해당 공개 범위를 찾을 수 없습니다."),
    GROUP_AUTHORITY_NOT_FOUND(NOT_FOUND, "해당 권한을 찾을 수 없습니다."),
    LEVEL_NOT_FOUND(NOT_FOUND, "해당 레벨을 찾을 수 없습니다."),
    TECHSTACK_NOT_FOUND(NOT_FOUND, "해당 기술 스택을 찾을 수 없습니다."),
    ROLE_NOT_FOUND(NOT_FOUND, "해당 역할을 찾을 수 없습니다."),
    APPLIY_FORM_NOT_FOUND(NOT_FOUND, "해당 신청서를 찾을 수 없습니다."),
    BOARD_NOT_FOUND(NOT_FOUND, "해당 게시판을 찾을 수 없습니다."),
    CONTENT_NOT_FOUND(NOT_FOUND, "내용을 찾을 수 없습니다."),
    ARTICLE_NOT_FOUND(NOT_FOUND, "게시글을 찾을 수 없습니다."),
    COMMENT_NOT_FOUND(NOT_FOUND, "해당 댓글을 찾을 수 없습니다."),
    MEMBER_CLUB_NOT_FOUND(NOT_FOUND, "해당 멤버를 클럽에서 찾을 수 없습니다."),

    /* 409 CONFLICT : Resource 의 현재 상태와 충돌. 보통 중복된 데이터 존재 */
    DUPLICATE_RESOURCE(CONFLICT, "데이터가 이미 존재합니다"),

    ;


    private final HttpStatus httpStatus;
    private final String detail;
}
