package com.mh.match.member.entity;

public enum AlarmTopic {
	APPLY_STUDY("alarm.study.apply"),
	APPROVE_JOIN_STUDY("alarm.study.join.approve"),
	REJECT_JOIN_STUDY("alarm.study.join.reject");

	private String messageKey;

	AlarmTopic(String messageKey) {
		this.messageKey = messageKey;
	}

	public String getMessageKey() {
		return messageKey;
	}
}