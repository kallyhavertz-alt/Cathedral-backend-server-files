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

}
class StaffLoginRequest {
    String getIdentity;
    private String password;
}
