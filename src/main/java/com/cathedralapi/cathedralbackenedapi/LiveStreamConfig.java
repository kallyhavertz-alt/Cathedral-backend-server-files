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

    // --- Getters and Setters ---
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String title;
    private String streamUrl;
    private boolean isLive;

    // 🎯 The critical addition: logs exactly when the video was posted
    private LocalDateTime updatedAt = LocalDateTime.now();

}
/* package com.cathedralapi.cathedralbackenedapi;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@Entity
@Table(name = "live_stream_config")
public class LiveStreamConfig {

    // Getters and Setters
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "stream_url", nullable = false)
    private String streamUrl;

    @Column(name = "is_live")
    private boolean isLive;

    @Column(name = "title")
    private String title;

    // Default Constructor (Required by JPA)
    public LiveStreamConfig() {
    }

}*/
