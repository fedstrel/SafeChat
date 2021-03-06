package com.example.safechat.controller;

import com.example.safechat.dto.RoomDTO;
import com.example.safechat.dto.WSMessageDTO;
import com.example.safechat.entity.Room;
import com.example.safechat.exception.PresenceNotFoundException;
import com.example.safechat.facade.RoomFacade;
import com.example.safechat.payload.response.MessageResponse;
import com.example.safechat.service.RoomService;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import net.minidev.json.JSONArray;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

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
    public ResponseEntity<WSMessageDTO> deleteUsersFromRoom(@RequestBody List<Long> userIds,
                                                       @PathVariable Long id,
                                                       @PathVariable Long userId) {
        try {
            Room room = roomService.deleteUsersFromRoom(userIds, id, userId);
            if (room == null)
                return new ResponseEntity<>(new WSMessageDTO("No right to delete from this room", ""), HttpStatus.OK);
            return new ResponseEntity<>(new WSMessageDTO("Users were deleted.", ""), HttpStatus.OK);
        } catch (PresenceNotFoundException e) {
            return new ResponseEntity<>(new WSMessageDTO(e.getMessage(), ""), HttpStatus.OK);
        }
    }

    @PostMapping("/leave/{roomId}")
    public ResponseEntity<MessageResponse> leaveRoom(@PathVariable Long roomId) {
        try {
            roomService.leaveRoom(roomId);
            return new ResponseEntity<>(new MessageResponse("Room has been successfully left."), HttpStatus.OK);
        } catch (RuntimeException e) {
            return new ResponseEntity<>(new MessageResponse(e.getMessage()), HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping("/user/{userId}/admin/{roomId}")
    public ResponseEntity<Boolean> isUserAdminOfTheRoom(@PathVariable Long roomId,
                                                        @PathVariable Long userId) {
        return new ResponseEntity<>(roomService.isUserAdminOfRoom(userId, roomId), HttpStatus.OK);
    }

    @GetMapping("/user/{userId}/present/{roomId}")
    public ResponseEntity<Boolean> isUserPresentInTheRoom(@PathVariable Long roomId,
                                                        @PathVariable Long userId) {
        return new ResponseEntity<>(roomService.isUserPresentInTheRoom(userId, roomId), HttpStatus.OK);
    }

    @GetMapping("/user/{userId}/not-visited")
    public ResponseEntity<List<RoomDTO>> getPublicRoomsThatAreNotVisited(@PathVariable Long userId) {
        List<RoomDTO> dtoList = roomService.getPublicRoomsThatAreNotVisited(userId)
                .stream()
                .map(roomFacade::roomToRoomDTO)
                .collect(Collectors.toList());
        return new ResponseEntity<>(dtoList, HttpStatus.OK);
    }
}
