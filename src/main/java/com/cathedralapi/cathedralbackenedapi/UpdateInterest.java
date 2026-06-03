package com.cathedralapi.cathedralbackenedapi;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import java.time.LocalDateTime;

@Entity
@Table(name = "update_interests")
@Getter
@Setter
public class UpdateInterest {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Column(name = "other_update", nullable = false, length = 79)
    private String otherUpdate;

    @Column(name = "clicked_at", nullable = false)
    private LocalDateTime clickedAt;
}