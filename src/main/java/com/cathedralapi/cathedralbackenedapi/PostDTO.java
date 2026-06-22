package com.cathedralapi.cathedralbackenedapi;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class PostDTO {
    // Getters and Setters
    private Long id;
    private String postType;
    private String subService;
    private String title;
    private String content;
    private String formattedDate;
    private String formattedTime;
    private String senderId;

}
