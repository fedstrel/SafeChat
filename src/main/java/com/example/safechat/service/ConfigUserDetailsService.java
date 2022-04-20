package com.example.safechat.service;

import com.example.safechat.entity.User;
import com.example.safechat.exception.UserNotFoundException;
import com.example.safechat.repository.IUserRepository;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;


@Service
public class ConfigUserDetailsService implements UserDetailsService {
    private final IUserRepository userRepository;

    public ConfigUserDetailsService(IUserRepository userRepository) {
        this.userRepository = userRepository;
    }

    //Search the user in DB
    @Override
    public UserDetails loadUserByUsername(String username){
        return userRepository.findUserByUsername(username)
                .orElseThrow(() -> new UserNotFoundException("Not found."));
    }

    public User loadUserById(Long id){
        return userRepository.findUserById(id).orElse(null);
    }
}
