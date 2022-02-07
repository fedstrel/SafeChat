package com.example.safechat.dto;

import com.example.safechat.entity.enums.EPublicityType;
import com.example.safechat.entity.enums.ERoomType;
import lombok.Data;

@Data
public class RoomDTO {
    private Long id;
    private String name;
    private ERoomType roomType;
    private EPublicityType publicityType;
}
