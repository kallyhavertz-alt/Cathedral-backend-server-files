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
    private String status = "";

    @Column(columnDefinition = "TEXT")
    private String description;

    @Column(name = "update_type", nullable = false, length = 50)
    private String updateType = "EVENTS";

    @Column(name = "notification_sent", nullable = false)
    private String notificationSent =  "";

    @Column(name = "featured_event_reading", nullable = false, length = 50)
    private String featuredEventReading = "";

    @Column(columnDefinition = "TEXT", length = 10000000)
    private String eventReadingText;

    @Column(name = "image_url", length = 500)
    private String imageUrl;

    public boolean isNotificationSent() {
        return notificationSent.equalsIgnoreCase("upcoming");
    }

}
