package com.cathedralapi.cathedralbackenedapi;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/users")
@CrossOrigin(origins = "*")
public class UserController {

    @Autowired
    private UserService userService; // 🛡️ Service defense layer successfully injected!

    // 📝 1. USER REGISTRATION ENDPOINT
    @PostMapping("/register")
    public ResponseEntity<?> registerUser(@RequestBody UserRegistrationDto registrationDto) {
        try {
            Map<String, Object> successResponse = userService.registerNewUser(registrationDto);
            return ResponseEntity.ok(successResponse);
        } catch (IllegalArgumentException e) {
            // Catches duplicate email checks explicitly
            return ResponseEntity.badRequest().body(Map.of("message", e.getMessage()));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("message", "An unexpected error occurred during registration."));
        }
    }

    // 🔑 2. USER LOGIN ENDPOINT
    @PostMapping("/login")
    public ResponseEntity<?> loginUser(@RequestBody LoginRequest loginRequest) {
        try {
            Map<String, Object> profileData = userService.authenticateUser(loginRequest);
            return ResponseEntity.ok(profileData);
        } catch (RuntimeException e) {
            // Returns 401 Unauthorized for bad emails/passwords uniformly to protect account privacy
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(Map.of("message", e.getMessage()));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("message", "An unexpected error occurred during login."));
        }
    }
}