package com.cathedralapi.cathedralbackenedapi;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class StaffRegistrationDTO {
    // Getters and Setters
    private String fullName;
    private String email;
    private String password;
    private String phoneNumber;
    private String ministeringLevel;

    public void setFullName(String fullName) { this.fullName = fullName; }

    public void setEmail(String email) { this.email = email; }

    public void setPassword(String password) { this.password = password; }

    public void setPhoneNumber(String phoneNumber) { this.phoneNumber = phoneNumber; }

    public void setMinisteringLevel(String ministeringLevel) { this.ministeringLevel = ministeringLevel; }
}
