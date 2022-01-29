package com.example.safechat.service;

import com.example.safechat.entity.User;
import com.example.safechat.entity.UserPresence;
import com.example.safechat.exception.PresenceNotFoundException;
import com.example.safechat.repository.IRoomRepository;
import com.example.safechat.repository.IUserPresenceRepository;
import com.example.safechat.repository.IUserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class UserService {
    private final IRoomRepository roomRepository;
    private final IUserRepository userRepository;
    private final IUserPresenceRepository userPresenceRepository;

    @Autowired
    public UserService(IRoomRepository roomRepository,
                       IUserRepository userRepository,
                       IUserPresenceRepository userPresenceRepository) {
        this.roomRepository = roomRepository;
        this.userRepository = userRepository;
        this.userPresenceRepository = userPresenceRepository;
    }

    public List<User> getAllUsersForRoom(Long roomId) {
        List<UserPresence> presences = userPresenceRepository.findAllByRoom(roomId)
                .orElseThrow(() -> new PresenceNotFoundException("Presence not found."));
        List<User> users = new ArrayList<>();
        for (UserPresence presence:
                presences) {
            users.add(userRepository.getById(presence.getUserId()));
        }
        return users;
    }
}
