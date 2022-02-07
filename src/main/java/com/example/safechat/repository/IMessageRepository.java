package com.example.safechat.repository;

import com.example.safechat.entity.Message;
import com.example.safechat.entity.compoundkey.UserPresenceId;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface IMessageRepository extends JpaRepository<Message, Long> {
    Optional<List<Message>> findAllByRoomId(Long roomId);
    Optional<Page<Message>> findAllByRoomId(Long roomId, Pageable pageable);
    Optional<List<Message>> findAllByRoomIdAndText(Long roomId, String text);
}
