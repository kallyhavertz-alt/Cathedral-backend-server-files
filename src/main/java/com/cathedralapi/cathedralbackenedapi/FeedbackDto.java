package com.cathedralapi.cathedralbackenedapi;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class FeedbackDto {

    // Getters and Setters
    @NotNull(message = "User ID cannot be missing")
    private Long userId;

    private String feedback; // Optional comment string

    @NotNull(message = "Star rating is required")
    @Min(value = 1, message = "Minimum rating is 1 star")
    @Max(value = 5, message = "Maximum rating is 5 stars")
    private Integer starsRated;

}