package com.cathedralapi.cathedralbackenedapi;

import com.cathedralapi.cathedralbackenedapi.Feedback;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;

@Repository
public interface FeedbackRepository extends JpaRepository<Feedback, Long> {

    boolean existsByUserId(Long userId);

    // Isolated query to look up all ratings left by a single user id if needed
    Optional<Feedback> findByUserId(Long userId);
}
