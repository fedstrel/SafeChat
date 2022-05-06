package com.example.safechat.repository;

import com.example.safechat.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface IUserRepository extends JpaRepository<User, Long> {
    Optional<User> findUserByUsername(String username);
    Optional<User> findUserById(Long id);
    @Query("SELECT u " +
            "FROM User u " +
            "WHERE u.username LIKE %:name% AND u.id NOT IN (SELECT up.user.id FROM UserPresence up WHERE up.room.id = :roomId)" +
            "GROUP BY u")
    Optional<List<User>>findAllContainingNameAndNotInTheRoom(@Param("name") String name, @Param("roomId") Long roomId);
}
