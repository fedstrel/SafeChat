package com.example.safechat.payload.response;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class JWTSuccessResponse {
    private boolean success;
    private String token;
}
