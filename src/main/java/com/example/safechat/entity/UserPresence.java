package com.example.safechat.entity;

import com.example.safechat.entity.enums.ERoomRole;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Data
@Table(name="user_presence")
public class UserPresence {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long userPresenceId;

    @Column(updatable = false)
    @JsonFormat(pattern = "yyyy-mm-dd HH:mm:s")
    private LocalDateTime joinDate;

    private ERoomRole role;

    @ManyToOne
    private User user;

    @ManyToOne
    private Room room;

    public UserPresence() { }
    public UserPresence(LocalDateTime joinDate,
                        ERoomRole role,
                        User user,
                        Room room) {
        this.user = user;
        this.room = room;
        this.joinDate = joinDate;
        this.role = role;
    }
}
