package com.cathedralapi.cathedralbackenedapi;

import com.cathedralapi.cathedralbackenedapi.StaffRegistrationDTO;
import com.cathedralapi.cathedralbackenedapi.StaffEntity;
import com.cathedralapi.cathedralbackenedapi.StaffRepository;
import com.cathedralapi.cathedralbackenedapi.StaffService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class StaffServiceImpl implements StaffService {

    @Autowired
    private StaffRepository staffRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    public void registerStaffMember(StaffRegistrationDTO dto) {
        // Prevent duplicate structural accounts
        if (staffRepository.findByEmail(dto.getEmail()).isPresent()) {
            throw new RuntimeException("This email is already registered as a Cathedral staff member.");
        }

        StaffEntity staff = new StaffEntity();
        staff.setFullName(dto.getFullName());
        staff.setEmail(dto.getEmail());
        staff.setPhoneNumber(dto.getPhoneNumber());
        staff.setMinisteringLevel(dto.getMinisteringLevel());

        // Securely hash password before writing to the database
        staff.setPassword(passwordEncoder.encode(dto.getPassword()));
        staff.setActiveStaff(true);

        staffRepository.save(staff);
    }
}