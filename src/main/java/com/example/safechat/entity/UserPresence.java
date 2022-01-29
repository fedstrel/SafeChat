package com.example.safechat.entity;

import com.example.safechat.entity.compoundkey.UserPresenceId;
import com.example.safechat.entity.enums.ERole;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.IdClass;
import java.time.LocalDateTime;

@Entity
@Data
@IdClass(UserPresenceId.class)
public class UserPresence {
    @Id
    private Long userId;

    @Id
    private Long roomId;

    @Column(updatable = false)
    @JsonFormat(pattern = "yyyy-mm-dd HH:mm:s")
    private LocalDateTime joinDate;

    private ERole role;
}
