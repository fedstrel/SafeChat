package com.example.safechat.facade;

import com.example.safechat.dto.MessageDTO;
import com.example.safechat.entity.Message;
import org.springframework.stereotype.Component;

@Component
public class MessageFacade {
    public MessageDTO messageToMessageDTO(Message message) {
        MessageDTO messageDTO = new MessageDTO();
        messageDTO.setId(message.getId());
        messageDTO.setText(message.getText());
        messageDTO.setCreationDate(message.getCreationDate());
        messageDTO.setFiles(message.getFiles());
        return messageDTO;
    }
}
