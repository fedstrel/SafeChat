package com.example.safechat.controller;

import com.example.safechat.dto.RoomDTO;
import com.example.safechat.entity.Room;
import com.example.safechat.facade.RoomFacade;
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

    @GetMapping("/{id}")
    public ResponseEntity<RoomDTO> getRoomById(@PathVariable Long id) {
        return new ResponseEntity<>(roomFacade.roomToRoomDTO(roomService.getRoomById(id)), HttpStatus.OK);
    }

    @GetMapping("/user/search/{userId}")
    public ResponseEntity<List<RoomDTO>> getAllRoomsByUserIdAndText(@PathVariable Long userId, @RequestBody String text) {
        List<RoomDTO> roomDTOList = roomService.getAllRoomsByUserContainingName(userId, text)
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

    @DeleteMapping("/{id}")
    @ResponseStatus(value = HttpStatus.OK)
    public void deleteRoom(@PathVariable Long id) {
        roomService.deleteRoom(id);
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

    @PostMapping("/delete/{id}")
    public ResponseEntity<RoomDTO> deleteUsersFromRoom(@RequestBody List<Long> userIds, @PathVariable Long id) {
        return new ResponseEntity<>(roomFacade.roomToRoomDTO(roomService.deleteUsersFromRoom(userIds, id)), HttpStatus.OK);
    }
}
