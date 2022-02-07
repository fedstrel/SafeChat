package com.example.safechat.facade;

import com.example.safechat.dto.UserDTO;
import com.example.safechat.dto.UserSecDTO;
import com.example.safechat.entity.User;
import org.springframework.stereotype.Component;

@Component
public class UserFacade {
    public UserDTO userToUserDTO(User user) {
        UserDTO userDTO = new UserDTO();
        userDTO.setId(user.getId());
        userDTO.setFirstname(user.getFirstname());
        userDTO.setLastname(user.getLastname());
        userDTO.setInfo(user.getInfo());
        return userDTO;
    }
    public UserSecDTO userToUserSecDTO(User user) {
        UserSecDTO userSecDTO = new UserSecDTO();
        userSecDTO.setId(user.getId());
        userSecDTO.setFirstname(user.getFirstname());
        userSecDTO.setLastname(user.getLastname());
        userSecDTO.setInfo(user.getInfo());
        userSecDTO.setEmail(user.getEmail());
        userSecDTO.setUsername(user.getUsername());
        userSecDTO.setPassword(user.getPassword());
        return userSecDTO;
    }
}
