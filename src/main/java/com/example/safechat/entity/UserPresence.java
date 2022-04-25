package com.example.safechat.entity;

import com.example.safechat.entity.enums.ERoomRole;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@Table(name="user_presence")
public class UserPresence {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long userPresenceId;

    @Column(updatable = false)
    @JsonFormat(pattern = "yyyy-mm-dd HH:mm:s")
    private LocalDateTime joinDate;

    private ERoomRole role;

    @ManyToOne(fetch = FetchType.LAZY)
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
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
