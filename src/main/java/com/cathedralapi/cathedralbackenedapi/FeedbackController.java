package com.cathedralapi.cathedralbackenedapi;

import com.cathedralapi.cathedralbackenedapi.FeedbackDto;
import com.cathedralapi.cathedralbackenedapi.Feedback;
import com.cathedralapi.cathedralbackenedapi.FeedbackService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/feedback")
public class FeedbackController {

    private final FeedbackService feedbackService;

    public FeedbackController(FeedbackService feedbackService) {
        this.feedbackService = feedbackService;
    }

    @PostMapping("/submit")
    public ResponseEntity<?> submitFeedback(@RequestBody FeedbackDto dto) {
        // 📡 Trace incoming packet transmission details immediately
        System.out.println("📡 FEEDBACK NETWORK HIT: Processing payload submission...");
        System.out.println("   -> Incoming User ID: " + dto.getUserId());
        System.out.println("   -> Rating Stars: " + dto.getStarsRated() + "/5");
        System.out.println("   -> Feedback Text: " + dto.getFeedback());

        try {
            Feedback savedRecord = feedbackService.saveFeedback(dto);
            System.out.println("✅ BACKEND PERSISTENCE SUCCESS: Saved feedback row ID: " + savedRecord.getId());
            return ResponseEntity.status(HttpStatus.CREATED).body(savedRecord);
        } catch (Exception e) {
            System.err.println("🚨 BACKEND ISOLATION FAILURE: Could not persist rating row: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Database rejection: " + e.getMessage());
        }
    }
}
