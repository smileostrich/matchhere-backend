package com.mh.match.chat.repository;


import com.mh.match.member.entity.Member;
import com.mh.match.chat.entity.ChatRoom;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ChatRoomRepository extends JpaRepository<ChatRoom, String> {
//    @Query(value = "select mc.id as id, mc.other_id as user_id, mc.other_nickname as user_nickname, mc.other_pic as img_uri from matching.chat_room mc where mc.other_id = :user_id")
//    List<ChatRoomUserInterface> findAllOthersByUser_id(@Param("user_id") Long user_id);
//
//    @Query(value = "select mc.id as id, mc.user_id as user_id, mc.user_nickname as user_nickname, mc.user_pic as img_uri from matching.chat_room mc where mc.user_id = :user_id")
//    List<ChatRoomUserInterface> findAllUsersByUser_id(@Param("user_id") Long user_id);

    List<ChatRoom> findAllByUser(Member user);
    List<ChatRoom> findAllByOther(Member other);
}
