package com.cathedralapi.cathedralbackenedapi;

import com.cathedralapi.cathedralbackenedapi.StaffRegistrationDTO;
import com.cathedralapi.cathedralbackenedapi.StaffService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.text.MessageFormat;
import java.util.Map;

@RestController
@RequestMapping("/api/v1/staff")
@CrossOrigin(origins = "*") // Allows access from mobile runtime setups
public class StaffPortalController {

    @Autowired
    private StaffService staffService;

    @Autowired
    private BCryptPasswordEncoder passwordEncoder;

    @PostMapping("/register")
    public ResponseEntity<?> processStaffRegistration(@RequestBody StaffRegistrationDTO registrationDTO) {
        try {
            staffService.registerStaffMember(registrationDTO);
            return ResponseEntity.ok(Map.of(
                    "status", "success",
                    "message", "Staff registration map processed successfully!"
            ));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Map.of(
                    "status", "error",
                    "message", e.getMessage()
            ));
        }
    }

    @PostMapping("/login")
    public ResponseEntity<?> loginStaffMember(@RequestBody StaffLoginRequest loginRequest) {
        // 1. Hard UI Gate: Reject null parsing payloads immediately
        if (loginRequest.getIdentity() == null || loginRequest.getPassword() == null) {
            System.out.println("🚨 Rejected malformed blank JSON payload intercept.");
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Map.of(
                    "status", "error",
                    "message", "Malformed payload: Identification parameters cannot be blank."
            ));
        }

        System.out.println("📡 Processing login attempt for identifier: " + loginRequest.getIdentity());

        // 2. Strict Authentication Chain Check against your PostgreSQL entity columns
        // Replace this with your actual database lookup (e.g., staffRepository.findByEmailOrPhoneNumber)
        boolean isAuthenticated = staffService.verifyStaffCredentials(
                loginRequest.getIdentity().trim(),
                loginRequest.getPassword()
        );

        if (isAuthenticated) {
            return ResponseEntity.ok(Map.of(
                    "status", "success",
                    "message", "Login verified successfully!"
            ));
        } else {
            // 🔒 Explicitly fail with an HTTP 401 Unauthorized block status code
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(Map.of(
                    "status", "error",
                    "message", "Invalid email/phone number or password."
            ));
        }
    }
}
