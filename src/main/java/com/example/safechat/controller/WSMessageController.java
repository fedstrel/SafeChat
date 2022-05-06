package com.example.safechat.controller;

import com.example.safechat.dto.MessageDTO;
import com.example.safechat.dto.WSMessageDTO;
import com.example.safechat.service.MessageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class WSMessageController {

    @Autowired
    private SimpMessagingTemplate simpMessagingTemplate;
    @Autowired
    private MessageService messageService;

    @MessageMapping("/chat/room/{roomId}")
    public void sendMessage(@DestinationVariable String roomId, WSMessageDTO wsMessageDTO) {
        MessageDTO message = new MessageDTO();
        message.setText(wsMessageDTO.getMessage());
        messageService.createMessage(Long.parseLong(roomId), Long.parseLong(wsMessageDTO.getSenderId()), message);
        simpMessagingTemplate.convertAndSend("/topic/messages/" + roomId, wsMessageDTO);
    }
}
