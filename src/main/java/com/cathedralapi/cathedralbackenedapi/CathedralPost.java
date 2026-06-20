
package com.cathedralapi.cathedralbackenedapi;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Setter
@Getter
@Entity
@Table(name = "cathedral_posts")
public class CathedralPost {
    // Getters and Setters
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String postType; // SUNDAY_SERVICE, MIDWEEK_EVENT, OTHER_MINISTRIES, ANNOUNCEMENT, BISHOP_SPECIAL

    @Column(nullable = false)
    private String subService; // KIKUYU, KISWAHILI, ENGLISH, MAIN_KIKUYU, NONE

    @Column(nullable = false)
    private String title;

    @Column(columnDefinition = "TEXT", nullable = false)
    private String content;

    @Column(nullable = false)
    private LocalDateTime createdAt = LocalDateTime.now();

}