package com.example.safechat.dto;

import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

@Data
public class MessageDTO {
    private Long id;
    private String text;
    private LocalDateTime creationDate;
    private List<String> files;
}
