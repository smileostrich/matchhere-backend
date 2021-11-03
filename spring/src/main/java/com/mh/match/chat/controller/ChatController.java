package com.mh.match.chat.controller;


import com.mh.match.chat.dto.ChatMessageResponseDto;
import com.mh.match.chat.dto.request.ChatMessageRequestDto;
import com.mh.match.chat.dto.response.ChatRoomResponseDto;
import com.mh.match.chat.dto.response.MemberChatRoomResponseDto;
import com.mh.match.chat.service.ChatService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.data.web.SortDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.web.bind.annotation.*;

import java.util.List;


//@CrossOrigin(origins = "http://localhost:3000")
@RequiredArgsConstructor
@RestController
@RequestMapping("/messages")
public class ChatController {
    private final ChatService chatService;

    @MessageMapping("/user/{id}")
    @SendTo("/room/{id}")
    public void sendMessage(ChatMessageRequestDto chatMessageRequestDto, @Header("Authorization") String token, @DestinationVariable Long id) throws Exception {
        chatService.sendMessage(chatMessageRequestDto, token, id);
    }

    @GetMapping("/chatroom/{id}")
    public ResponseEntity<Page<ChatMessageResponseDto>> getChattingHistory(@PathVariable("id") String id,
                                                                           @PageableDefault(size = 10) @SortDefault(sort = "sentTime", direction = Sort.Direction.DESC) Pageable pageable) throws Exception {
        return ResponseEntity.ok(chatService.getHistory(id, pageable));
    }

    @GetMapping("/chatroom")
    public ResponseEntity<List<ChatRoomResponseDto>> getChattingRooms() throws Exception {
        return ResponseEntity.ok(chatService.getChattingRooms());
    }

    @GetMapping("/member/{email}")
    public ResponseEntity<MemberChatRoomResponseDto> getMemberChat(@PathVariable("email") String email) throws Exception {
        return ResponseEntity.ok(chatService.getChatroomId(email));
    }
}
