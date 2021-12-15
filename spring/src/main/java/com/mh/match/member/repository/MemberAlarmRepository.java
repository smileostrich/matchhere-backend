package com.mh.match.member.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.mh.match.member.entity.Alarm;
import com.mh.match.member.entity.AlarmContent;

@Repository
public interface MemberAlarmRepository extends JpaRepository<Alarm, Long> {
	List<AlarmContent> findTop10ByMemberIdOrderByRegistYmdtDesc(Long memberId);

	Alarm findFirstById(Long id);
}