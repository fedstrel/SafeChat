package com.example.safechat.service;

import com.example.safechat.dto.PageDTO;
import com.example.safechat.entity.Message;
import com.example.safechat.exception.MessageNotFoundException;
import com.example.safechat.repository.IMessageRepository;
import com.example.safechat.repository.IRoomRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class MessageService {
    private final IRoomRepository roomRepository;
    private final IMessageRepository messageRepository;

    public MessageService(IRoomRepository roomRepository,
                          IMessageRepository messageRepository) {
        this.roomRepository = roomRepository;
        this.messageRepository = messageRepository;
    }

    public List<Message> getAllMessagesByRoom(Long roomId) {
        return messageRepository.findAllByRoomId(roomId).orElseThrow(() -> new MessageNotFoundException("Message not found."));
    }

    public List<Message> getMessagesByRoomPageable(Long roomId, PageDTO page) {
        return messageRepository.findAllByRoomId(roomId, page.getPageable())
                .orElseThrow(() -> new MessageNotFoundException("Message not found."))
                .getContent();
    }
}
