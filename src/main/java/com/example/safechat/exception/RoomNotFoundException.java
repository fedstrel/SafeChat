package com.example.safechat.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class RoomNotFoundException extends RuntimeException{
    public RoomNotFoundException(String mes) {
        super(mes);
    }
}
