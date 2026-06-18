package com.cathedralapi.cathedralbackenedapi;

import com.cathedralapi.cathedralbackenedapi.StaffRegistrationDTO;

public interface StaffService {
    void registerStaffMember(StaffRegistrationDTO registrationDTO);

    boolean verifyStaffCredentials(String trim, String password);
}
