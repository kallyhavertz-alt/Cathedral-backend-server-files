package com.cathedralapi.cathedralbackenedapi;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Setter
@Getter
@Entity
@Table(name = "staff_notices")
public class StaffNotice {
    // Getters and Setters
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(columnDefinition = "TEXT", nullable = false)
    private String content;

    @Column(nullable = false)
    private LocalDateTime createdAt = LocalDateTime.now();

    @Column(name = "sender_id")
    private String senderId;

}
