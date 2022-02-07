package com.example.safechat.service;

import com.example.safechat.entity.User;
import com.example.safechat.entity.UserPresence;
import com.example.safechat.exception.PresenceNotFoundException;
import com.example.safechat.repository.IMessageRepository;
import com.example.safechat.repository.IUserPresenceRepository;
import com.example.safechat.repository.IUserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class UserService {
    private final IMessageRepository messageRepository;
    private final IUserRepository userRepository;
    private final IUserPresenceRepository userPresenceRepository;

    @Autowired
    public UserService(IMessageRepository messageRepository,
                       IUserRepository userRepository,
                       IUserPresenceRepository userPresenceRepository) {
        this.messageRepository = messageRepository;
        this.userRepository = userRepository;
        this.userPresenceRepository = userPresenceRepository;
    }

    public List<User> getAllUsersForRoom(Long roomId) {
        List<UserPresence> presences = userPresenceRepository.findAllByRoomId(roomId)
                .orElseThrow(() -> new PresenceNotFoundException("Presence not found."));
        List<User> users = new ArrayList<>();
        for (UserPresence presence:
                presences) {
            users.add(userRepository.getById(presence.getUser().getId()));
        }
        return users;
    }

    public User getUserByMessage(Long mesId) {
        return messageRepository.getById(mesId).getUser();
    }
}
