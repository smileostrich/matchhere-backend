package com.mh.match.common.entity;

import com.mh.match.common.exception.CustomException;
import com.mh.match.common.exception.ErrorCode;

public enum GroupAuthority {
    소유자, 관리자, 팀원;

    public static GroupAuthority from(String s){
        GroupAuthority groupAuthority;
        if(s.equals("소유자")){
            groupAuthority = GroupAuthority.소유자;
        }else if(s.equals("관리자")){
            groupAuthority = GroupAuthority.관리자;
        }else if(s.equals("팀원")){
            groupAuthority = GroupAuthority.팀원;
        }else {
            throw new CustomException(ErrorCode.GROUP_AUTHORITY_NOT_FOUND);
        }
        return groupAuthority;
    }

}
