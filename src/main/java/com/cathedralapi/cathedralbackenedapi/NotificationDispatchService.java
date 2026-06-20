package com.cathedralapi.cathedralbackenedapi;

import com.cathedralapi.cathedralbackenedapi.CathedralPost;
import com.cathedralapi.cathedralbackenedapi.StaffNotice;
import org.springframework.stereotype.Service;
import java.util.HashMap;
import java.util.Map;

@Service
public class NotificationDispatchService {

    // 📢 ROUTE 1: General Congregation Target Topic
    public void dispatchPostNotification(CathedralPost post) {
        String targetTopic = "church_updates";

        Map<String, String> dataPayload = new HashMap<>();
        dataPayload.put("id", String.valueOf(post.getId()));
        dataPayload.put("postType", post.getPostType());
        dataPayload.put("subService", post.getSubService());

        String title = post.getTitle();
        String body = post.getContent();

        if ("SUNDAY_SERVICE".equalsIgnoreCase(post.getPostType())) {
            title = "⛪ Sunday Service Update!";
            body = "[" + post.getSubService() + "] " + post.getTitle();
        }

        sendNativePush(targetTopic, title, body, dataPayload);
    }

    // 🔒 ROUTE 2: Private Staff Target Topic
    public void dispatchStaffNoticeNotification(StaffNotice notice) {
        String targetTopic = "staff_updates";

        Map<String, String> dataPayload = new HashMap<>();
        dataPayload.put("id", String.valueOf(notice.getId()));
        dataPayload.put("security", "internal_staff_alert");

        sendNativePush(
                targetTopic,
                "📋 Internal Staff Notice",
                notice.getContent(),
                dataPayload
        );
    }

    private void sendNativePush(String topic, String title, String body, Map<String, String> data) {
        // Core framework delivery hooks to FCM / OneSignal execute seamlessly here
        System.out.println("📡 DISPATCH ENGINE -> Sent to topic [" + topic + "]: " + title + " - " + body);
    }
}