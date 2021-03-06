package com.example.safechat.repository;

import com.example.safechat.entity.UserPresence;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface IUserPresenceRepository extends JpaRepository<UserPresence, Long> {
    Optional<List<UserPresence>> findAllByUserId(Long userId);
    Optional<List<UserPresence>> findAllByRoomId(Long roomId);
    Optional<UserPresence> findByRoomIdAndUserId(Long roomId, Long userId);
}
