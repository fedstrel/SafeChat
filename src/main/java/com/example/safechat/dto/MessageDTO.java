package com.example.safechat.dto;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class MessageDTO {
    private Long id;
    private String text;
    private LocalDateTime creationDate;
}
