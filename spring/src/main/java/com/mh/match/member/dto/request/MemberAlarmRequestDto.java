package com.mh.match.member.dto.request;

import static com.mh.match.util.CommonConstants.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.mh.match.member.entity.Alarm;
import com.mh.match.member.entity.AlarmTopic;
import lombok.Getter;
import lombok.NoArgsConstructor;


@Getter
@NoArgsConstructor
public class MemberAlarmRequestDto {
    private Long memberId;
    private AlarmTopic alarmTopic;
    private List<String> alarmParam;

    @JsonIgnore
    public Alarm toAlarm() {
        return Alarm.builder()
                .memberId(memberId)
                .alarmTopic(alarmTopic)
                .alarmParam(alarmParam.stream().collect(Collectors.joining(PIPE)))
                .isRead(false)
                .registYmdt(LocalDateTime.now())
                .build();
    }
}
