package com.mh.match.chat.entity;


import com.mh.match.member.entity.Member;
import lombok.*;

import javax.persistence.*;

@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity(name = "matching.chat_room")
public class ChatRoom {
    @Id
    private String id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private Member user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "other_id")
    private Member other;

//    private int unReadCount;

    @Builder
    public ChatRoom(String id, Member user, Member other) {
        this.id = id;
        this.user = user;
        this.other = other;
    }
}
