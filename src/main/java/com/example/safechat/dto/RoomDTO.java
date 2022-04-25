package com.example.safechat.dto;

import com.example.safechat.entity.enums.EPublicityType;
import com.example.safechat.entity.enums.ERoomType;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class RoomDTO {
    private Long id;
    private String name;
    private LocalDateTime creationDate;
    private ERoomType roomType;
    private EPublicityType publicityType;
}
