package com.cathedralapi.cathedralbackenedapi;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Setter
@Getter
@Entity
@Table(name = "live_stream_config")
public class LiveStreamConfig {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String title;

    @Column(name = "stream_url")
    private String streamUrl;

    @Column(name = "is_live")
    private boolean isLive;

    // THE SYSTEM BIT TRACKER: Maps explicitly to PostgreSQL columns
    @Column(name = "notification_sent")
    private boolean notificationSent = false;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt = LocalDateTime.now();
}
