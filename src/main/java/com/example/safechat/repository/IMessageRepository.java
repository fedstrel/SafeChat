package com.example.safechat.repository;

import com.example.safechat.entity.Message;
import com.example.safechat.entity.compoundkey.UserPresenceId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface IMessageRepository extends JpaRepository<Message, Long> {
    Optional<List<Message>> findAllByRoom(Long roomId);
    Optional<List<Message>> findAllByRoomAndText(Long roomId, String text);
}
