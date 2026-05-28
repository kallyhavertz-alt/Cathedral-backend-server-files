package com.cathedralapi.cathedralbackenedapi;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class NoteDto {
    // Getters and Setters
    private Long userId;
    private Long eventId;
    private String eventTitle;
    private String title;
    private String content;

}