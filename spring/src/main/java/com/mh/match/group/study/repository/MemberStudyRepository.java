package com.mh.match.group.study.repository;

import com.mh.match.member.entity.Member;
import com.mh.match.group.study.entity.CompositeMemberStudy;
import com.mh.match.group.study.entity.MemberStudy;
import com.mh.match.group.study.entity.Study;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface MemberStudyRepository extends JpaRepository<MemberStudy, CompositeMemberStudy> {

    // 특정 스터디에 속한 멤버의 관계 정보
    @Query(value = "select ms from matching.member_study ms "
        + "where ms.compositeMemberStudy.study = :study and ms.isActive = true")
    List<MemberStudy> findMemberRelationInStudy(@Param("study") Study study);

    // 특정 스터디에 속한 멤버의 정보
    @Query(value = "select ms.compositeMemberStudy.member from matching.member_study ms "
        + "where ms.compositeMemberStudy.study = :study and ms.isActive = true")
    List<Member> findMemberInStudy(@Param("study") Study study);

    // 특정 멤버가 가지고 있는 활성화 스터디
    @Query(value = "select ms.compositeMemberStudy.study from matching.member_study ms "
        + "where ms.compositeMemberStudy.member = :member and ms.isActive = true")
    List<Study> studyInMember(@Param("member") Member member);

    void deleteAllByCompositeMemberStudy_Member(Member member);

}