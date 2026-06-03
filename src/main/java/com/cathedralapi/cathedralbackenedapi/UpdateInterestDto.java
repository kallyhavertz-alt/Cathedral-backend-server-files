package com.cathedralapi.cathedralbackenedapi;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class UpdateInterestDto {

    // Standard Clean Setter
    // Standard Clean Getter
    @NotBlank(message = "Feedback cannot be completely empty!")
    @Size(max = 79, message = "Your request details should not be more than 79 characters!")
    private String otherUpdate;

}