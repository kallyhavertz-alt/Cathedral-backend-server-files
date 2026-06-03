package com.cathedralapi.cathedralbackenedapi;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import jakarta.validation.Valid;
import java.time.LocalDateTime;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/api/v1/updates")
public class UpdateInterestController {

    @Autowired
    private UpdateInterestRepository interestRepository;

    @Autowired
    private UserRepository userRepository;

    @GetMapping("/check-status/{userId}")
    public ResponseEntity<?> checkStatus(@PathVariable Long userId) {
        System.out.println("📡 API INCOMING STATUS CHECK FOR USER ID: " + userId);
        boolean status = interestRepository.hasThisUserSubmitted(userId);
        System.out.println("📊 RESULT FOR USER ID " + userId + " IS: " + status);
        return ResponseEntity.ok(Map.of("hasSubmitted", status));
    }

    @PostMapping("/register-interest/{userId}")
    public ResponseEntity<?> registerInterest(
            @PathVariable Long userId,
            @Valid @RequestBody UpdateInterestDto dto) {

        System.out.println("📥 SAVING NEW ENTRY FOR USER ID: " + userId);

        Optional<User> userOpt = userRepository.findById(userId);
        if (userOpt.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(Map.of("message", "User account not found."));
        }

        UpdateInterest record = new UpdateInterest();
        record.setUser(userOpt.get());
        record.setOtherUpdate(dto.getOtherUpdate());
        record.setClickedAt(LocalDateTime.now());


        interestRepository.save(record);
        return ResponseEntity.ok(Map.of("message", "Preferences logged successfully."));
    }
}