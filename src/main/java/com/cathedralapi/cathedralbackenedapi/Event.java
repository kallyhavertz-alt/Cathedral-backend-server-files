package com.cathedralapi.cathedralbackenedapi;

import jakarta.persistence.*;
import java.time.LocalDateTime;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@Entity
@Table(name = "events")
public class Event {

    // Getters and Setters
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "event_title", nullable = false, length = 150)
    private String eventTitle;

    @Column(name = "event_date", nullable = false)
    private LocalDateTime eventDate;

    @Column(nullable = false, length = 100)
    private String venue;

    @Column(nullable = false, length = 50)
    private String status = "upcoming"; // Default value matching your sketch logic

    @Column(columnDefinition = "TEXT")
    private String description;

}
