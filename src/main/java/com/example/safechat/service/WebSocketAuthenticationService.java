package com.example.safechat.service;

import com.example.safechat.repository.IUserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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
    public static final Logger LOG = LoggerFactory.getLogger(WebSocketAuthenticationService.class);
    private final IUserRepository userRepository;

    @Autowired
    public WebSocketAuthenticationService(IUserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public UsernamePasswordAuthenticationToken getAuthToken(String username, String password) throws AuthenticationException {
        if (username == null || username.trim().isEmpty()) {
            LOG.info("Failed to auth webSocket. Username was null or empty.");
            throw new AuthenticationCredentialsNotFoundException("Username was null or empty.");
        }
        if (password == null || password.trim().isEmpty()) {
            LOG.info("Failed to auth webSocket. Password for user " + username + " was null or empty.");
            throw new AuthenticationCredentialsNotFoundException("Password was null or empty.");
        }
        if (userRepository.findUserByUsername(username).isEmpty()) {
            LOG.info("Failed to auth webSocket. User with username=" + username + " not found");
            throw new BadCredentialsException("User with username=" + username + " not found");
        }

        LOG.info("WebSocket authenticated for user=" + username);
        return new UsernamePasswordAuthenticationToken(
                username,
                null,
                Collections.singleton((GrantedAuthority) () -> "USER")
        );
    }
}
