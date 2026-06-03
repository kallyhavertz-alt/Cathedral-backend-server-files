package com.cathedralapi.cathedralbackenedapi;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@Service // 🎯 Marks this as the dedicated user account security guard
public class UserService {

    @Autowired
    private UserRepository userRepository;

    // Instance of BCrypt encoder isolated here inside the service layer
    private final BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    // 💾 1. SECURE REGISTRATION LOGIC
    public Map<String, Object> registerNewUser(UserRegistrationDto registrationDto) {
        // Check if the user email already exists
        Optional<User> existingUser = userRepository.findByEmail(registrationDto.getEmail());
        if (existingUser.isPresent()) {
            throw new IllegalArgumentException("Email is already registered standard error");
        }

        // Map DTO to Database Entity
        User newUser = new User();
        newUser.setFullName(registrationDto.getFullName());
        newUser.setEmail(registrationDto.getEmail());
        newUser.setFellowshipGroup(registrationDto.getFellowshipGroup());
        newUser.setResidentialCell(registrationDto.getResidentialCell());

        // 🔒 Hash password immediately before it goes anywhere near the database
        String secureHash = passwordEncoder.encode(registrationDto.getPassword());
        newUser.setPasswordHash(secureHash);

        userRepository.save(newUser);

        // Build the return data payload
        Map<String, Object> response = new HashMap<>();
        response.put("id", newUser.getId());
        response.put("message", "User account successfully created inside St. James Cathedral database!");
        response.put("fullName", newUser.getFullName());
        response.put("email", newUser.getEmail());

        return response;
    }

    // 🔑 2. SECURE LOGIN LOGIC
    public Map<String, Object> authenticateUser(LoginRequest loginRequest) {
        // Look up by email
        User user = userRepository.findByEmail(loginRequest.getEmail())
                .orElseThrow(() -> new RuntimeException("Invalid email or password credentials."));

        // 🔒 Verify BCrypt matching safely
        if (!passwordEncoder.matches(loginRequest.getPassword(), user.getPasswordHash())) {
            throw new RuntimeException("Invalid email or password credentials.");
        }

        User savedUser = userRepository.saveAndFlush(user);

        // Package the session profile payload for Flutter
        Map<String, Object> response = new HashMap<>();
        response.put("id", savedUser.getId());
        response.put("fullName", user.getFullName());
        response.put("email", user.getEmail());
        response.put("fellowshipGroup", user.getFellowshipGroup());
        response.put("residentialCell", user.getResidentialCell());

        return response;
    }
}
