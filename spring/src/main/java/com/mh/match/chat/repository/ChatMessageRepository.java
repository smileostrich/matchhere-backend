package com.mh.match.chat.repository;


import com.mh.match.chat.entity.ChatMessage;
import com.mh.match.chat.entity.ChatRoom;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ChatMessageRepository extends JpaRepository<ChatMessage, Long> {
//    @Query(value = "select mm.content as content , mm.sent_time as sent_time, mm.nickname as nickname, mm.sender_id as sender_id from matching.chat_message mm where mm.chatRoom = :chatRoom")
//    List<ChatMessageInterface> findAllByRoom(@Param("chatRoom") ChatRoom chatRoom);

//    Page<ChatMessage> findAllByChatRoom(ChatRoom chatRoom, Pageable pageable);
    Page<ChatMessage> findAllByChatRoom(ChatRoom chatRoom, Pageable pageable);
    ChatMessage findTopByChatRoomOrderBySentTimeDesc(ChatRoom chatRoom);
}
