package com.example.safechat.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class PresenceNotFoundException extends RuntimeException{
    public PresenceNotFoundException(String mes) {
        super(mes);
    }
}
