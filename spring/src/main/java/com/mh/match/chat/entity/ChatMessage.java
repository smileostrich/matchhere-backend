package com.mh.match.chat.entity;

import com.mh.match.member.entity.Member;
import lombok.*;

import javax.persistence.*;
import java.time.LocalDateTime;

@Getter
@Setter
@ToString
@Entity(name = "matching.chat_message")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ChatMessage {
    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    private Long id;

    private String content;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "sender_id")
    private Member sender;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "chat_room_id")
    private ChatRoom chatRoom;

    @Column(name = "sent_time", nullable = false)
    private LocalDateTime sentTime;
//    private Boolean is_read;

    @Builder
    public ChatMessage(String content, Member sender, ChatRoom chatRoom, LocalDateTime sentTime) {
        this.content = content;
        this.sender = sender;
        this.chatRoom = chatRoom;
        this.sentTime = sentTime;
    }

    //    public ChatMessage(String message, String sender) {
//        this.sender = sender;
//        this.message = message;
//    }

//    public ChatMessage(String fileName, String rawData, String sender) {
//        this.fileName = fileName;
//        this.rawData = rawData;
//        this.sender = sender;
//    }

//    public ChatMessage(String message) {
//        this.message = message;
//    }
}
