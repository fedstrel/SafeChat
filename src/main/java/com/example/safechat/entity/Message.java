package com.example.safechat.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Data
public class Message {
    @Id
    private Long id;

    @Column(nullable = false)
    private String text;

    @Column(updatable = false)
    @JsonFormat(pattern = "yyyy-mm-dd HH:mm:s")
    private LocalDateTime creationDate;

    @ElementCollection
    private List<String> files = new ArrayList<>();

    @ManyToOne
    private User user;

    @ManyToOne
    private Room room;
}
