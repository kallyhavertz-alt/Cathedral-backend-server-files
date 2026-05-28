package com.cathedralapi.cathedralbackenedapi;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/api/users")
@CrossOrigin(origins = "*")
public class UserController {

    @Autowired
    private UserRepository userRepository;

    private final BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    @PostMapping("/register")
    public ResponseEntity<?> registerUser(@RequestBody UserRegistrationDto registrationDto) {

        // 1. Check if the user email already exists in pgAdmin
        Optional<User> existingUser = userRepository.findByEmail(registrationDto.getEmail());
        if (existingUser.isPresent()) {
            return ResponseEntity.badRequest().body(Map.of("message", "Email is already registered standard error"));
        }

        // 2. Map the incoming data packet directly to our clean database User Entity
        User newUser = new User();
        newUser.setFullName(registrationDto.getFullName());
        newUser.setEmail(registrationDto.getEmail());
        newUser.setFellowshipGroup(registrationDto.getFellowshipGroup());
        newUser.setResidentialCell(registrationDto.getResidentialCell());

        // 3. 🔒 BCrypt Hashing Mode: Pull raw password text from DTO, encrypt it, then set it to entity
        String secureHash = passwordEncoder.encode(registrationDto.getPassword());
        newUser.setPasswordHash(secureHash);

        // 4. Commit row records permanently to PostgreSQL
        userRepository.save(newUser);

        // 5. Success Return!
        return ResponseEntity.ok(Map.of(
                "message", "User account successfully created inside St. James Cathedral database!",
                "fullName", newUser.getFullName(),
                "email", newUser.getEmail()
        ));
    }

    @PostMapping("/login")
    public ResponseEntity<?> loginUser(@RequestBody LoginRequest loginRequest) {
        // 1. Check if the user exists by email
        Optional<User> userOpt = userRepository.findByEmail(loginRequest.getEmail());

        if (userOpt.isEmpty()) {
            // Return 401 Unauthorized if email is wrong
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(Map.of("message", "Invalid email or password credentials."));
        }

        User user = userOpt.get();

        // 2. 🔒 Secure BCrypt matching verification
        if (!passwordEncoder.matches(loginRequest.getPassword(), user.getPasswordHash())) {
            // Return 401 Unauthorized if password doesn't match
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(Map.of("message", "Invalid email or password credentials."));
        }
        User savedUser = userRepository.saveAndFlush(user);

        // 3. Success! Return 200 OK along with user profile details for Flutter to save
        Map<String, Object> response = new HashMap<>();
        response.put("id", savedUser.getId());
        response.put("fullName", user.getFullName());
        response.put("email", user.getEmail());
        response.put("fellowshipGroup", user.getFellowshipGroup());
        response.put("residentialCell", user.getResidentialCell());

        return ResponseEntity.ok(response);
    }
}