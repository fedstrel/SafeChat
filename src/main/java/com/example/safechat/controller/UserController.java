package com.example.safechat.controller;

import com.example.safechat.dto.UserDTO;
import com.example.safechat.dto.UserSecDTO;
import com.example.safechat.facade.UserFacade;
import com.example.safechat.payload.response.MessageResponse;
import com.example.safechat.service.UserService;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
import java.util.stream.Collectors;

@AllArgsConstructor
@CrossOrigin
@Controller
@RequestMapping("/user")
public class UserController {

    @Autowired
    private final UserService userService;
    @Autowired
    private final UserFacade userFacade;

    @GetMapping("/cur")
    public ResponseEntity<UserSecDTO> getCurrentUser() {
        return new ResponseEntity<>(userFacade.userToUserSecDTO(userService.getCurrentUser()), HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<UserDTO> getUserById(@PathVariable Long id) {
        return new ResponseEntity<>(userFacade.userToUserDTO(userService.getUserById(id)), HttpStatus.OK);
    }

    @GetMapping("/message/{mesId}")
    public ResponseEntity<UserDTO> getUserByMessageId(@PathVariable Long mesId) {
        return new ResponseEntity<>(userFacade.userToUserDTO(userService.getUserByMessage(mesId)), HttpStatus.OK);
    }

    @GetMapping("/room/{roomId}")
    public ResponseEntity<List<UserDTO>> getAllUsersForRoom(@PathVariable Long roomId) {
        List<UserDTO> userDTOList = userService.getAllUsersForRoom(roomId)
                .stream()
                .map(userFacade::userToUserDTO)
                .collect(Collectors.toList());
        return new ResponseEntity<>(userDTOList, HttpStatus.OK);
    }

    @PostMapping("/update")
    public ResponseEntity<UserSecDTO> updateUser(@Valid @RequestBody UserSecDTO userSecDTO) {
        return new ResponseEntity<>(userFacade.userToUserSecDTO(userService.updateUser(userSecDTO)), HttpStatus.OK);
    }

    @PostMapping("/{userId}/delete")
    public ResponseEntity<MessageResponse> deleteUser(@PathVariable Long userId) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth != null && auth.getAuthorities().stream().anyMatch(a -> a.getAuthority().equals("ADMIN"))) {
            userService.deleteUser(userId);
            return new ResponseEntity<>(new MessageResponse("The user " + userId + " was deleted."), HttpStatus.OK);
        }
        return new ResponseEntity<>(new MessageResponse("Not enough rights to delete user."), HttpStatus.UNAUTHORIZED);
    }

    @PostMapping("/cur/delete")
    public ResponseEntity<MessageResponse> deleteCurUser() {
        userService.deleteUser(userService.getCurrentUser().getId());
        return new ResponseEntity<>(new MessageResponse("User has been deleted"), HttpStatus.OK);
    }
}
