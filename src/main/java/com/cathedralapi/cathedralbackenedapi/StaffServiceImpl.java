package com.cathedralapi.cathedralbackenedapi;

import com.cathedralapi.cathedralbackenedapi.StaffRegistrationDTO;
import com.cathedralapi.cathedralbackenedapi.StaffEntity;
import com.cathedralapi.cathedralbackenedapi.StaffRepository;
import com.cathedralapi.cathedralbackenedapi.StaffService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class StaffServiceImpl implements StaffService {

    @Autowired
    private StaffRepository staffRepository;
    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    public void registerStaffMember(StaffRegistrationDTO dto) {
        // 🚀 OPTIMIZATION 1: Secure both Email AND Phone number uniqueness
        if (staffRepository.findByEmail(dto.getEmail().trim()).isPresent()) {
            throw new RuntimeException("This email is already registered as a Cathedral staff member.");
        }
        if (staffRepository.findByPhoneNumber(dto.getPhoneNumber().trim()).isPresent()) {
            throw new RuntimeException("This phone number is already registered to a staff member.");
        }

        StaffEntity staff = new StaffEntity();
        staff.setFullName(dto.getFullName().trim());
        staff.setEmail(dto.getEmail().trim().toLowerCase()); // Keep emails lowercase for clean indexing
        staff.setPhoneNumber(dto.getPhoneNumber().trim());
        staff.setMinisteringLevel(dto.getMinisteringLevel());

        // Securely hash password before writing to the database
        staff.setPassword(passwordEncoder.encode(dto.getPassword()));
        staff.setActiveStaff(true);

        staffRepository.save(staff);
    }

    @Override
    public boolean verifyStaffCredentials(String identity, String password) {
        // 🚀 OPTIMIZATION 2: Query database dynamically using either Email or Phone Number
        // (Ensure you add findByEmailOrPhoneNumber to your StaffRepository interface)
        Optional<StaffEntity> staffOpt = staffRepository.findByEmailOrPhoneNumber(identity.trim(), identity.trim());

        if (staffOpt.isEmpty()) {
            System.out.println("🔍 Database Lookup: No record found matching identifier: " + identity);
            return false;
        }

        StaffEntity staff = staffOpt.get();

        // Verify account is active before authenticating
        if (!staff.isActiveStaff()) {
            System.out.println("🔒 Security Guard: Access blocked. Account status is inactive.");
            return false;
        }

        // 🚀 OPTIMIZATION 3: Safely decode and match BCrypt hash variables
        boolean passwordMatches = passwordEncoder.matches(password, staff.getPassword());

        if (!passwordMatches) {
            System.out.println("🔒 Security Guard: Password validation failed for " + identity);
        }

        return passwordMatches;
    }
   }