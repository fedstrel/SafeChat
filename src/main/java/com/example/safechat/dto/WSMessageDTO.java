package com.example.safechat.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class WSMessageDTO {
    private String message;
    private String senderId;
}
