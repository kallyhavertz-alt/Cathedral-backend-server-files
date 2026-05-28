package com.cathedralapi.cathedralbackenedapi; // Adjust to your package structure

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class LoginRequest {
    // Getters and Setters
    private String email;
    private String password;

}