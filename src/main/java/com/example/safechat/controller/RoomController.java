package com.example.safechat.controller;

import com.example.safechat.dto.RoomDTO;
import com.example.safechat.facade.RoomFacade;
import com.example.safechat.service.RoomService;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@CrossOrigin
@RestController
@RequestMapping("/room")
public class RoomController {

    @Autowired
    private RoomService roomService;
    @Autowired
    private RoomFacade roomFacade;

    @GetMapping("/{id}")
    public ResponseEntity<RoomDTO> getRoomById(@PathVariable Long id) {
        return new ResponseEntity<>(roomFacade.roomToRoomDTO(roomService.getRoomById(id)), HttpStatus.OK);
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<List<RoomDTO>> getAllRoomsByUserId(@PathVariable Long userId) {
        List<RoomDTO> roomDTOList = roomService.getAllRoomsForUser(userId)
                .stream()
                .map(roomFacade::roomToRoomDTO)
                .collect(Collectors.toList());
        return new ResponseEntity<>(roomDTOList, HttpStatus.OK);
    }
}
