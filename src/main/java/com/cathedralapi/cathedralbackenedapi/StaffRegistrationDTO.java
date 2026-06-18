package com.cathedralapi.cathedralbackenedapi;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.api.client.util.ArrayMap;
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
@Setter
@Getter
class StaffLoginRequest {
    @JsonProperty("identity")
    private String identity;

    @JsonProperty("password")
    private String password;
    String getIdentity;


}
