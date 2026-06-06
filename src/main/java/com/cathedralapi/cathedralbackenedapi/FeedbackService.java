package com.cathedralapi.cathedralbackenedapi;

import com.cathedralapi.cathedralbackenedapi.FeedbackDto;
import com.cathedralapi.cathedralbackenedapi.Feedback;
import com.cathedralapi.cathedralbackenedapi.FeedbackRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.time.LocalDateTime;
import java.util.Optional;

@Service
public class FeedbackService {

    private final FeedbackRepository feedbackRepository;

    public FeedbackService(FeedbackRepository feedbackRepository) {
        this.feedbackRepository = feedbackRepository;
    }

    @Transactional
    public Feedback saveFeedback(FeedbackDto dto) {
        Optional<Feedback> existingFeedbackOpt = feedbackRepository.findByUserId(dto.getUserId());
        if (existingFeedbackOpt.isPresent()) {
            // 🔥 UPDATE PATH: User already voted. Overwrite their previous submission.
            Feedback existingFeedback = existingFeedbackOpt.get();
            System.out.println("🔄 BACKEND LOGIC: User " + dto.getUserId() + " already rated. Overwriting existing Row ID: " + existingFeedback.getId());

            existingFeedback.setStarsRated(dto.getStarsRated());
            existingFeedback.setFeedback(dto.getFeedback() != null ? dto.getFeedback().trim() : "");
            existingFeedback.setSubmittedAt(LocalDateTime.now()); // Update timestamp to latest push

            // Hibernate performs an UPDATE SQL statement automatically on managed entities
            return feedbackRepository.save(existingFeedback);
        } else {
            // ✨ INSERT PATH: Brand  user feedback entry
            System.out.println("✨ BACKEND LOGIC: Creating fresh feedback entry for User ID: " + dto.getUserId());
            Feedback newFeedback = new Feedback(
                    dto.getUserId(),
                    dto.getFeedback() != null ? dto.getFeedback().trim() : "",
                    LocalDateTime.now(),
                    dto.getStarsRated()
            );

            return feedbackRepository.save(newFeedback);
        }
    }
}

