package com.example.safechat.service;

import com.example.safechat.repository.IUserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationCredentialsNotFoundException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Component;

import java.util.Collections;

@Component
public class WebSocketAuthenticationService {

    private final IUserRepository userRepository;

    @Autowired
    public WebSocketAuthenticationService(IUserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public UsernamePasswordAuthenticationToken getAuthToken(String username, String password) throws AuthenticationException {
        if (username == null || username.trim().isEmpty()) {
            throw new AuthenticationCredentialsNotFoundException("Username was null or empty.");
        }
        if (password == null || password.trim().isEmpty()) {
            throw new AuthenticationCredentialsNotFoundException("Password was null or empty.");
        }
        if (userRepository.findUserByUsername(username).isEmpty()) {
            throw new BadCredentialsException("User with username=" + username + " not found");
        }

        return new UsernamePasswordAuthenticationToken(
                username,
                null,
                Collections.singleton((GrantedAuthority) () -> "USER")
        );
    }
}
