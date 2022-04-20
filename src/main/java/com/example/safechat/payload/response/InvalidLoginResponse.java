package com.example.safechat.payload.response;

public class InvalidLoginResponse {
    final private String username;
    final private String password;

    public InvalidLoginResponse() {
        this.username = "Invalid username";
        this.password = "Invalid password";
    }
}
