package com.cathedralapi.cathedralbackenedapi;

import com.cathedralapi.cathedralbackenedapi.CathedralPost;
import com.cathedralapi.cathedralbackenedapi.StaffNotice;
import com.google.firebase.messaging.FirebaseMessaging;
import com.google.firebase.messaging.Message;
import com.google.firebase.messaging.Notification;
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

        // Beautifully format layout banners for Sunday services
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

    // 🚀 LIVE FIREBASE PUSH TRANSMISSION ENGINE
    private void sendNativePush(String topic, String title, String body, Map<String, String> data) {
        try {
            // Trim down very long descriptions so they read perfectly in the phone notification tray
            String displayBody = body.length() > 120 ? body.substring(0, 117) + "..." : body;

            // Build the visual notification component
            Notification notification = Notification.builder()
                    .setTitle(title)
                    .setBody(displayBody)
                    .build();

            // Construct the complete Firebase Messaging packet
            Message message = Message.builder()
                    .setTopic(topic)
                    .setNotification(notification)
                    .putAllData(data) // Background data parameters for handling click routes in Flutter
                    .build();

            // Execute asynchronous non-blocking thread delivery to Google FCM servers
            FirebaseMessaging.getInstance().sendAsync(message);
            System.out.println("📡 DISPATCH ENGINE -> Production notification sent to topic [" + topic + "]");

        } catch (Exception e) {
            System.err.println("❌ FCM Delivery Failure for topic [" + topic + "]: " + e.getMessage());
        }
    }
}