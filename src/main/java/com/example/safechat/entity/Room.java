package com.example.safechat.entity;

import com.example.safechat.entity.enums.EPublicityType;
import com.example.safechat.entity.enums.ERole;
import com.example.safechat.entity.enums.ERoomType;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Data
@Entity
public class Room {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    @Column(updatable = false)
    @JsonFormat(pattern = "yyyy-mm-dd HH:mm:s")
    private LocalDateTime createDate;

    private ERoomType roomType;
    private EPublicityType publicityType;

    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "room")
    private List<UserPresence> userPresenceList = new ArrayList<>();

    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "room")
    private List<Message> messages = new ArrayList<>();

    public List<UserPresence> addPresenceToUserPresenceList(UserPresence presence) {
        userPresenceList.add(presence);
        return userPresenceList;
    }
}
