package com.example.safechat.repository;

import com.example.safechat.entity.UserPresence;
import com.example.safechat.entity.compoundkey.UserPresenceId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface IUserPresenceRepository extends JpaRepository<UserPresence, UserPresenceId> {
    Optional<List<UserPresence>> findAllByUser(Long userId);
    Optional<List<UserPresence>> findAllByRoom(Long roomId);
}
