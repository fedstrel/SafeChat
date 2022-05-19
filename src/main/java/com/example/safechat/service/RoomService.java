package com.example.safechat.service;

import com.example.safechat.dto.RoomDTO;
import com.example.safechat.entity.Room;
import com.example.safechat.entity.User;
import com.example.safechat.entity.UserPresence;
import com.example.safechat.entity.enums.ERoomRole;
import com.example.safechat.exception.PresenceNotFoundException;
import com.example.safechat.exception.RoomNotFoundException;
import com.example.safechat.exception.UserNotFoundException;
import com.example.safechat.repository.IRoomRepository;
import com.example.safechat.repository.IUserPresenceRepository;
import com.example.safechat.repository.IUserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class RoomService {
    public static final Logger LOG = LoggerFactory.getLogger(RoomService.class);
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

    public Room getRoomById(Long roomId) {
        return roomRepository.findById(roomId).orElseThrow(() -> new RoomNotFoundException("Room not found"));
    }

    public Room createRoom(Long userId, RoomDTO dto) {
        //creating an instance of a room
        Room room = new Room();
        room.setName(dto.getName());
        room.setPublicityType(dto.getPublicityType());
        room.setRoomType(dto.getRoomType());
        room.setCreateDate(LocalDateTime.now());
        room = roomRepository.save(room);
        LOG.info("Created a room called " + room.getName());
        //connecting a room to other entities in the database
        UserPresence presence = new UserPresence();
        presence.setRoom(room);
        presence.setUser(userRepository.getById(userId));
        presence.setRole(ERoomRole.ROOM_ROLE_ADMIN);
        presence.setJoinDate(room.getCreateDate());
        userPresenceRepository.save(presence);
        LOG.info("Added a user with userId=" + userId + " to room called " + room.getName());

        //saving connected room
        room.addPresenceToUserPresenceList(presence);
        return roomRepository.save(room);
    }

    public boolean deleteRoom(Long roomId, Long userId) {
        if (!isUserAdminOfRoom(userId, roomId)) {
            LOG.info("Deletion refused due to the lack of rights.");
            return false;
        }
        Room room = roomRepository.getById(roomId);
        Optional<List<UserPresence>> presences = userPresenceRepository.findAllByRoomId(roomId);
        roomRepository.delete(room);
        presences.ifPresent((userPresenceRepository::deleteAll));
        LOG.info("Room called " + room.getName() + " with id=" + roomId + " was deleted.");
        return true;
    }

    public Room addUsersToRoom(List<Long> userIds, Long roomId) {
        LocalDateTime joinDate = LocalDateTime.now();
        List<UserPresence> presences = new ArrayList<>();
        for (Long userId:
             userIds) {
            UserPresence presence = new UserPresence(joinDate,
                    ERoomRole.ROOM_ROLE_USER,
                    userRepository.getById(userId),
                    roomRepository.getById(roomId));
            presences.add(presence);
            User user = userRepository.getById(userId);
            user.getUserPresenceList().add(presence);
            userRepository.save(user);
            LOG.info("User with id=" + userId + " has been added to the room with id=" + roomId);
        }
        Room room = roomRepository.getById(roomId);
        room.addPresencesToUserPresenceList(presences);
        return room;
    }

    public void leaveRoom(Long roomId) {
        User user = (User)SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        UserPresence presence = userPresenceRepository.findByRoomIdAndUserId(roomId, user.getId())
                .orElseThrow(() -> new UserNotFoundException("User is not present in the room."));
        userPresenceRepository.delete(presence);
        LOG.info("User with id=" + user.getId() + " has left the room with id=" + roomId);
    }

    public Room deleteUsersFromRoom(List<Long> userIds, Long roomId, Long userAuthorId) {
        if (!isUserAdminOfRoom(userAuthorId, roomId))
            return null;
        Room room = roomRepository.getById(roomId);
        List<UserPresence> presences = room.getUserPresenceList();
        for (Long userId:
             userIds) {
            UserPresence presence = userPresenceRepository.findByRoomIdAndUserId(roomId, userId)
                    .orElseThrow(() -> new PresenceNotFoundException("UserPresence for user with id=" + userId + " not found."));
            presences.remove(presence);
            room.setUserPresenceList(presences);
            roomRepository.save(room);
            userPresenceRepository.delete(presence);
            LOG.info("User with id=" + userId + " has been deleted from the room with roomId=" + roomId);
        }
        return room;
    }

    public List<Room> getAllRoomsForUser(Long userId) {
        List<UserPresence> presences = userPresenceRepository.findAllByUserId(userId)
                .orElseThrow(() -> new PresenceNotFoundException("Presence not found."));
        List<Room> rooms = new ArrayList<>();
        for (UserPresence presence:
                presences) {
            rooms.add(roomRepository.getById(presence.getRoom().getId()));
        }
        return rooms;
    }

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

   public List<Room> getAllRoomsContainingName(String name) {
        return roomRepository.findAllContainingName(name)
                .orElseThrow(() -> new RoomNotFoundException("Room not found."));
   }

    public List<Room> getPublicRoomsThatAreNotVisited(Long userId) {
        List<Room> rooms = roomRepository.findAllByUserIdAndPublicityType(userId)
                .orElseThrow(() -> new RoomNotFoundException("Rooms were not found"));
        List<Room> resRooms = new ArrayList<>();
        for (Room room: rooms) {
            if (!presenceListContainsUserId(room.getUserPresenceList(), userId)) {
                resRooms.add(room);
            }
        }
        return resRooms;
    }

   public boolean isUserAdminOfRoom(Long userId, Long roomId) {
       UserPresence userAuthorPresence = userPresenceRepository.findByRoomIdAndUserId(roomId, userId)
               .orElseThrow(() -> new UserNotFoundException("User not found"));
       return userAuthorPresence.getRole() == ERoomRole.ROOM_ROLE_ADMIN;
   }

   public boolean isUserPresentInTheRoom(Long userId, Long roomId) {
       Optional<UserPresence> userAuthorPresence = userPresenceRepository.findByRoomIdAndUserId(roomId, userId);
       return userAuthorPresence.isPresent();
   }

   private boolean presenceListContainsUserId(List<UserPresence> presenceList, Long userId) {
        for (UserPresence presence: presenceList) {
            if (presence.getUser().getId() == userId)
                return true;
        }
        return false;
   }
}
