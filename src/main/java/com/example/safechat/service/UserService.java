package com.example.safechat.service;

import com.example.safechat.dto.UserSecDTO;
import com.example.safechat.entity.User;
import com.example.safechat.entity.UserPresence;
import com.example.safechat.entity.enums.ERole;
import com.example.safechat.exception.PresenceNotFoundException;
import com.example.safechat.exception.RoomNotFoundException;
import com.example.safechat.exception.UserAlreadyExistsException;
import com.example.safechat.exception.UserNotFoundException;
import com.example.safechat.payload.request.SignupRequest;
import com.example.safechat.repository.IMessageRepository;
import com.example.safechat.repository.IUserPresenceRepository;
import com.example.safechat.repository.IUserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.security.Principal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
public class UserService {
    public static final Logger LOG = LoggerFactory.getLogger(UserService.class);
    private final IMessageRepository messageRepository;
    private final IUserRepository userRepository;
    private final IUserPresenceRepository userPresenceRepository;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;

    @Autowired
    public UserService(IMessageRepository messageRepository,
                       IUserRepository userRepository,
                       IUserPresenceRepository userPresenceRepository,
                       BCryptPasswordEncoder bCryptPasswordEncoder) {
        this.messageRepository = messageRepository;
        this.userRepository = userRepository;
        this.userPresenceRepository = userPresenceRepository;
        this.bCryptPasswordEncoder = bCryptPasswordEncoder;
    }

    public User createUser(SignupRequest signupRequestRequest) {
        if (userRepository.findUserByUsername(signupRequestRequest.getUsername()).isPresent()) {
            LOG.info("Failed to create a user. Entered username is already in use");
            throw new UserAlreadyExistsException("Entered username is already in use");
        }
        User user = new User();
        user.setEmail(signupRequestRequest.getEmail());
        user.setFirstname(signupRequestRequest.getFirstname());
        user.setLastname(signupRequestRequest.getLastname());
        user.setUsername(signupRequestRequest.getUsername());
        user.setPassword(bCryptPasswordEncoder.encode(signupRequestRequest.getPassword()));
        user.setRole(ERole.ROLE_USER);
        user.setInfo(signupRequestRequest.getInfo());
        user.setCreateDate(LocalDateTime.now());

        LOG.info("User " + user.getFirstname() + " " + user.getLastname() + " with username=" + user.getUsername() + " created");
        return userRepository.save(user);
    }

    public User updateUser(UserSecDTO userSecDTO) {
        User user = userRepository.findUserById(userSecDTO.getId())
                .orElseThrow(() -> new UserNotFoundException("User not found."));

        user.setId(userSecDTO.getId());
        user.setFirstname(userSecDTO.getFirstname());
        user.setLastname(userSecDTO.getLastname());
        user.setEmail(userSecDTO.getEmail());
        user.setUsername(userSecDTO.getUsername());
        user.setPassword(userSecDTO.getPassword());
        user.setRole(userSecDTO.getRole());
        user.setCreateDate(LocalDateTime.now());

        LOG.info("User " + user.getFirstname() + " " + user.getLastname() + " with username=" + user.getUsername() + " updated");
        return userRepository.save(user);
    }

    public void deleteUser(Long userId) {
        User user = userRepository.getById(userId);
        userRepository.delete(user);
        LOG.info("User " + user.getFirstname() + " " + user.getLastname() + " with username=" + user.getUsername() + " deleted");
    }

    public User getUserById(Long userId) {
        return userRepository.findById(userId).orElseThrow(() -> new UserNotFoundException("User not found."));
    }

    public List<User> getAllUsersContainingNameAndNotInTheRoom(String name, Long roomId) {
        return userRepository.findAllContainingNameAndNotInTheRoom(name, roomId)
                .orElseThrow(() -> new UserNotFoundException("user not found"));
    }

    public List<User> getAllUsersForRoom(Long roomId) {
        List<UserPresence> presences = userPresenceRepository.findAllByRoomId(roomId)
                .orElseThrow(() -> new RoomNotFoundException("Room not found."));
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

    public User getCurrentUser() {
        return (User)SecurityContextHolder.getContext().getAuthentication().getPrincipal();
    }
}
