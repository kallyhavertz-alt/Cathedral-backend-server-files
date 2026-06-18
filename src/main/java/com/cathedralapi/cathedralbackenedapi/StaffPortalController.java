package com.cathedralapi.cathedralbackenedapi;

import com.cathedralapi.cathedralbackenedapi.StaffRegistrationDTO;
import com.cathedralapi.cathedralbackenedapi.StaffService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.text.MessageFormat;
import java.util.Map;

@RestController
@RequestMapping("/api/v1/staff")
@CrossOrigin(origins = "*") // Allows access from mobile runtime setups
public class StaffPortalController {

    @Autowired
    private StaffService staffService;

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
        System.out.println(MessageFormat.format("\uD83D\uDCE1 Processing login attempt for identifier: {0}", loginRequest.getIdentity));

        // TODO: Wire up your authentication logic here via your StaffService layer
        // Example: Verification of user identity against database rows and password match check
        boolean isAuthenticated = true; // Placeholder for your DB service authentication step

        if (isAuthenticated) {
            return ResponseEntity.ok(Map.of(
                    "status", "success",
                    "message", "Login verified successfully!"
            ));
        } else {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(Map.of(
                    "status", "error",
                    "message", "Invalid credentials supplied."
            ));
        }
    }
}
