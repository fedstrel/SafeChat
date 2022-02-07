package com.example.safechat.facade;

import com.example.safechat.dto.RoomDTO;
import com.example.safechat.entity.Room;
import org.springframework.stereotype.Component;

@Component
public class RoomFacade {
    public RoomDTO roomToRoomDTO(Room room) {
        RoomDTO roomDTO = new RoomDTO();
        roomDTO.setId(room.getId());
        roomDTO.setRoomType(room.getRoomType());
        roomDTO.setName(room.getName());
        roomDTO.setPublicityType(room.getPublicityType());
        return roomDTO;
    }
}
