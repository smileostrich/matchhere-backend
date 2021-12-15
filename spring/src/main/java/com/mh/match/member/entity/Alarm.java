package com.mh.match.member.entity;

import java.time.LocalDateTime;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@Entity(name = "matching.member_alarm")
@NoArgsConstructor
public class Alarm {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	@Column(name = "member_id", nullable = false)
	private Long memberId;
	@Column(name = "alarm_topic", nullable = false)
	@Enumerated(EnumType.STRING)
	private AlarmTopic alarmTopic;
	@Column(name = "alarm_param")
	private String alarmParam;
	@Column(name = "is_read", nullable = false)
	private Boolean isRead;
	@Column(name = "regist_ymdt", nullable = false)
	private LocalDateTime registYmdt;

	@Builder
	public Alarm(Long memberId, AlarmTopic alarmTopic, String alarmParam, Boolean isRead, LocalDateTime registYmdt) {
		this.memberId = memberId;
		this.alarmTopic = alarmTopic;
		this.alarmParam = alarmParam;
		this.isRead = isRead;
		this.registYmdt = registYmdt;
	}
}