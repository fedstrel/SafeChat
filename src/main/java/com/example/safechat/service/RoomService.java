package com.example.safechat.service;

import com.example.safechat.dto.RoomDTO;
import com.example.safechat.entity.Room;
import com.example.safechat.entity.User;
import com.example.safechat.entity.UserPresence;
import com.example.safechat.entity.enums.ERole;
import com.example.safechat.exception.PresenceNotFoundException;
import com.example.safechat.repository.IRoomRepository;
import com.example.safechat.repository.IUserPresenceRepository;
import com.example.safechat.repository.IUserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class RoomService {
    private final IRoomRepository roomRepository;
    private final IUserRepository userRepository;
    private final IUserPresenceRepository userPresenceRepository;

    @Autowired
    public RoomService(IRoomRepository roomRepository,
                       IUserRepository userRepository,
                       IUserPresenceRepository userPresenceRepository) {
        this.roomRepository = roomRepository;
        this.userRepository = userRepository;
        this.userPresenceRepository = userPresenceRepository;
    }

    public Room createRoom(Long userId, RoomDTO dto) {
        //creating an instance of a room
        Room room = new Room();
        room.setName(dto.getName());
        room.setPublicityType(dto.getPublicityType());
        room.setRoomType(dto.getRoomType());
        room.setCreateDate(LocalDateTime.now());
        room = roomRepository.save(room);
        /*
        Вопрос: я читал что JPARepository работают с Unit of Work,
        то есть чтобы в последствии обновить созданную комнату,
        мне надо бы получить ее через поиск, чтобы она начала отслеживаться
        контекстом. Достаточно ли вышеприведенного присваивания, чтобы контекст начал
        отслеживать сущность или необходимо будет сделать вот такое присваивание:
        room = roomRepository.getById(roomRepository.save(room).getId())?
        */

        //connecting a room to other entities in the database
        UserPresence presence = new UserPresence();
        presence.setRoom(room);
        presence.setUser(userRepository.getById(userId));
        presence.setRole(ERole.ROLE_ADMIN);
        presence.setJoinDate(room.getCreateDate());
        userPresenceRepository.save(presence);

        //saving connected room
        room.addPresenceToUserPresenceList(presence);
        return roomRepository.save(room);
    }

    public void deleteRoom(Long roomId) {
        Room room = roomRepository.getById(roomId);
        Optional<List<UserPresence>> presences = userPresenceRepository.findAllByRoomId(roomId);
        roomRepository.delete(room);
        presences.ifPresent((userPresenceRepository::deleteAll));
    }

    public Room addUsersToRoom(List<Long> userIds, Long roomId) {
        LocalDateTime joinDate = LocalDateTime.now();
        List<UserPresence> presences = new ArrayList<>();
        for (Long userId:
             userIds) {
            UserPresence presence = new UserPresence(joinDate,
                    ERole.ROLE_USER,
                    userRepository.getById(userId),
                    roomRepository.getById(roomId));
            presences.add(presence);
            User user = userRepository.getById(userId);
            user.getUserPresenceList().add(presence);
            userRepository.save(user);
        }
        userPresenceRepository.saveAll(presences);
        Room room = roomRepository.getById(roomId);
        room.addPresencesToUserPresenceList(presences);
        return room;
    }

    //vulnerability: can drop when one room is being deleted while querying others
    public List<Room> getAllRoomsForUser(Long userId) {
        List<UserPresence> presences = userPresenceRepository.findAllByUserId(userId)
                .orElseThrow(() -> new PresenceNotFoundException("Presence not found."));
        List<Room> rooms = new ArrayList<>();
        for (UserPresence presence:
                presences) {
            rooms.add(roomRepository.getById(presence.getUser().getId()));
        }
        return rooms;
    }

    //vulnerability: can drop when one room is being deleted while querying others
    public List<Room> getAllRoomsByUserContainingName(Long userId, String name) {
        List<Room> rooms = getAllRoomsForUser(userId);
        List<Room> resRooms = new ArrayList<>();
        for (Room room:
             rooms) {
            if (room.getName().contains(name))
                resRooms.add(room);
        }
        return resRooms;
    }

    //wait for sensei's answer and add getAllRoomsContainingName(String name) { }
}
