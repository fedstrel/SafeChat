package com.example.safechat.entity;

import com.example.safechat.entity.enums.EPublicityType;
import com.example.safechat.entity.enums.ERoomType;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import lombok.Data;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Data
@Entity
@Table(name="rooms")
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler", "userPresenceList", "messages"})
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

    public List<UserPresence> addPresencesToUserPresenceList(List<UserPresence> presences) {
        userPresenceList.addAll(presences);
        return userPresenceList;
    }

    @Override
    public String toString() {
        return id + " " + name;
    }
}
