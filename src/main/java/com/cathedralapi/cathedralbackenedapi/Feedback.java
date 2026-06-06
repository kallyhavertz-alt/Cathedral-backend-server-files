package com.cathedralapi.cathedralbackenedapi;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Setter
@Getter
@Entity
@Table(name = "feedbacks")
public class Feedback {

    // Getters and Setters
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "user_id", nullable = false)
    private Long userId;

    @Column(name = "feedback", length = 1000)
    private String feedback;

    @Column(name = "submitted_at", nullable = false)
    private LocalDateTime submittedAt;

    @Column(name = "stars_rated", nullable = false)
    private Integer starsRated;

    // Constructors
    public Feedback() {}

    public Feedback(Long userId, String feedback, LocalDateTime submittedAt, Integer starsRated) {
        this.userId = userId;
        this.feedback = feedback;
        this.submittedAt = submittedAt;
        this.starsRated = starsRated;
    }

}
