package com.example.safechat.entity.compoundkey;

import java.io.Serializable;
import java.util.Objects;

public class UserPresenceId implements Serializable {
    private Long userId;
    private Long roomId;

    public UserPresenceId() { }

    public UserPresenceId(Long userId, Long roomId) {
        this.userId = userId;
        this.roomId = roomId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        UserPresenceId that = (UserPresenceId) o;
        return Objects.equals(userId, that.userId) && Objects.equals(roomId, that.roomId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(userId, roomId);
    }
}
