package com.example.safechat.repository;

import com.example.safechat.entity.Room;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface IRoomRepository extends JpaRepository<Room, Long> {
    @Query("SELECT r FROM Room r WHERE r.publicityType = 0 AND r.name LIKE %:name%")
    Optional<List<Room>> findAllContainingName(@Param("name") String name);
}
