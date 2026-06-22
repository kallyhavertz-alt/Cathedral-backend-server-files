package com.cathedralapi.cathedralbackenedapi;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class NoticeDTO {
    private Long id;
    private String content;
    private String formattedDate;
    private String formattedTime;
    private String senderId;
    private String senderName;
    private String fullName;

}
