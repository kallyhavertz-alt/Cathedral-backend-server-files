package com.cathedralapi.cathedralbackenedapi;

import lombok.Data;

@Data
public class UserRegistrationDto {
    private String fullName;
    private String email;
    private String password;
    private String residentialCell;
    private String fellowshipGroup;
}