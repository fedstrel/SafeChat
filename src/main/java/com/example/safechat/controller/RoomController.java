package com.example.safechat.controller;

import com.example.safechat.dto.RoomDTO;
import com.example.safechat.entity.Room;
import com.example.safechat.facade.RoomFacade;
import com.example.safechat.payload.response.MessageResponse;
import com.example.safechat.service.RoomService;
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

    @GetMapping("/search={name}")
    public ResponseEntity<List<RoomDTO>> getAllRoomsContainingName(@PathVariable String name) {
        List<RoomDTO> roomDTOList = roomService.getAllRoomsContainingName(name)
                .stream()
                .map(roomFacade::roomToRoomDTO)
                .collect(Collectors.toList());
        return new ResponseEntity<>(roomDTOList, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<RoomDTO> getRoomById(@PathVariable Long id) {
        return new ResponseEntity<>(roomFacade.roomToRoomDTO(roomService.getRoomById(id)), HttpStatus.OK);
    }

    @GetMapping("/search={name}/{userId}")
    public ResponseEntity<List<RoomDTO>> getAllRoomsByUserIdAndName(@PathVariable Long userId, @PathVariable String name) {
        List<RoomDTO> roomDTOList = roomService.getAllRoomsByUserContainingName(userId, name)
                .stream()
                .map(roomFacade::roomToRoomDTO)
                .collect(Collectors.toList());
        return new ResponseEntity<>(roomDTOList, HttpStatus.OK);
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<List<RoomDTO>> getAllRoomsByUserId(@PathVariable Long userId) {
        List<RoomDTO> roomDTOList = roomService.getAllRoomsForUser(userId)
                .stream()
                .map(roomFacade::roomToRoomDTO)
                .collect(Collectors.toList());
        return new ResponseEntity<>(roomDTOList, HttpStatus.OK);
    }

    @DeleteMapping("/{id}/user/{userId}")
    @ResponseStatus(value = HttpStatus.OK)
    public ResponseEntity<MessageResponse> deleteRoom(@PathVariable Long id, @PathVariable Long userId) {
        if (roomService.deleteRoom(id, userId))
            return new ResponseEntity<>(new MessageResponse("Room deleted"), HttpStatus.OK);
        return new ResponseEntity<>(new MessageResponse("Not enough rights to delete room"), HttpStatus.UNAUTHORIZED);
    }

    @PostMapping("/create/{userId}")
    public ResponseEntity<RoomDTO> createRoom(@RequestBody RoomDTO roomDTO, @PathVariable Long userId) {
        Room room = roomService.createRoom(userId, roomDTO);
        return new ResponseEntity<>(roomFacade.roomToRoomDTO(room), HttpStatus.OK);
    }

    @PostMapping("/add/{id}")
    public ResponseEntity<RoomDTO> addUsersToRoom(@RequestBody List<Long> userIds, @PathVariable Long id) {
        return new ResponseEntity<>(roomFacade.roomToRoomDTO(roomService.addUsersToRoom(userIds, id)), HttpStatus.OK);
    }

    @PostMapping("/delete/{id}/user/{userId}")
    public ResponseEntity<RoomDTO> deleteUsersFromRoom(@RequestBody List<Long> userIds,
                                                       @PathVariable Long id,
                                                       @PathVariable Long userId) {
        Room room = roomService.deleteUsersFromRoom(userIds, id, userId);
        if (room == null)
            return new ResponseEntity<>(new RoomDTO(), HttpStatus.UNAUTHORIZED);
        return new ResponseEntity<>(roomFacade.roomToRoomDTO(room), HttpStatus.OK);
    }
}
