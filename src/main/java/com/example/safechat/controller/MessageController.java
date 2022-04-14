package com.example.safechat.controller;

import com.example.safechat.dto.MessageDTO;
import com.example.safechat.dto.PageDTO;
import com.example.safechat.entity.Message;
import com.example.safechat.facade.MessageFacade;
import com.example.safechat.service.MessageService;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@AllArgsConstructor
@RestController
@RequestMapping("/message")
public class MessageController {

    @Autowired
    private MessageService messageService;
    @Autowired
    private MessageFacade messageFacade;

    @GetMapping("/room/{roomId}")
    public ResponseEntity<Page<Message>> getMessagesByRoomPageable(@PathVariable Long roomId, PageDTO pageDTO) {
        Page<Message> page = new PageImpl<>(messageService.getMessagesByRoomPageable(roomId, pageDTO));
        return new ResponseEntity<>(page, HttpStatus.OK);
    }

    @GetMapping("/room/all/{roomId}")
    public ResponseEntity<List<Message>> getAllMessagesByRoom(@PathVariable Long roomId) {
        return new ResponseEntity<>(messageService.getAllMessagesByRoom(roomId), HttpStatus.OK);
    }

    @PostMapping("/create/{roomId}/{userId}")
    public ResponseEntity<MessageDTO> createMessage(@PathVariable Long roomId,
                                                    @PathVariable Long userId,
                                                    @RequestBody MessageDTO messageDTO) {
        Message message = messageService.createMessage(roomId, userId, messageDTO);
        return new ResponseEntity<>(messageFacade.messageToMessageDTO(message), HttpStatus.CREATED);
    }
}
