package com.example.safechat.service;

import com.example.safechat.dto.MessageDTO;
import com.example.safechat.dto.PageDTO;
import com.example.safechat.entity.Message;
import com.example.safechat.exception.MessageNotFoundException;
import com.example.safechat.repository.IMessageRepository;
import com.example.safechat.repository.IRoomRepository;
import com.example.safechat.repository.IUserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class MessageService {
    public static final Logger LOG = LoggerFactory.getLogger(MessageService.class);
    private final IRoomRepository roomRepository;
    private final IUserRepository userRepository;
    private final IMessageRepository messageRepository;

    public MessageService(IRoomRepository roomRepository,
                          IUserRepository userRepository,
                          IMessageRepository messageRepository) {
        this.roomRepository = roomRepository;
        this.userRepository = userRepository;
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

    public Message createMessage(Long roomId, Long userId, MessageDTO messageDTO)
    {
        if (messageDTO.getText().length() == 0)
            return null;
        Message message = new Message();
        message.setText(messageDTO.getText());
        message.setCreationDate(LocalDateTime.now());
        message.setFiles(messageDTO.getFiles());

        message.setUser(userRepository.getById(userId));

        message.setRoom(roomRepository.getById(roomId));

        LOG.info("Message send from userId=" + userId + " to room with roomId=" + roomId);
        return messageRepository.save(message);
    }
}
