package com.cathedralapi.cathedralbackenedapi;

import com.google.firebase.messaging.FirebaseMessaging;
import com.google.firebase.messaging.AndroidConfig;
import com.google.firebase.messaging.AndroidNotification;
import com.google.firebase.messaging.Message;
import com.google.firebase.messaging.Notification;
import org.springframework.stereotype.Service;
import java.util.HashMap;
import java.util.Map;

@Service
public class NotificationDispatchService {

    // 🔒 THE DATA CONTRACT CONSTANTS
    public static final String KEY_TITLE = "title";
    public static final String KEY_BODY = "body";
    public static final String KEY_CATEGORY = "category_type"; // Unified Key Name
    public static final String KEY_ID = "record_id";

    // 📢 ROUTE 1: General Congregation Target Topic
    public void dispatchPostNotification(CathedralPost post) {
        String targetTopic = "church_updates";

        Map<String, String> dataPayload = new HashMap<>();
        dataPayload.put(KEY_ID, String.valueOf(post.getId()));
        dataPayload.put(KEY_CATEGORY, post.getPostType().toUpperCase()); // e.g., SUNDAY_SERVICE

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
        dataPayload.put(KEY_ID, String.valueOf(notice.getId()));
        dataPayload.put(KEY_CATEGORY, "STAFF_NOTICE"); // Explicit Category Identifier

        sendNativePush(
                targetTopic,
                "📋 Internal Staff Notice",
                notice.getContent(),
                dataPayload
        );
    }

    // 🚀 FIREBASE TRANSMISSION ENGINE
    private void sendNativePush(String topic, String title, String body, Map<String, String> data) {
        try {
            String displayBody = body.length() > 120 ? body.substring(0, 117) + "..." : body;

            // Explicitly inject the title and body into the data payload map as well
            // This ensures if the phone drops the visual block, the data mapping saves it!
            data.put(KEY_TITLE, title);
            data.put(KEY_BODY, displayBody);
            AndroidConfig androidConfig = AndroidConfig.builder()
                    .setPriority(AndroidConfig.Priority.HIGH) // 🚀 Wakes up aggressive battery savers
                    .setNotification(AndroidNotification.builder()
                            .setSound("default")
                            .setChannelId("high_importance_channel") // Matches default Flutter channels
                            .build())
                    .build();

            Notification notification = Notification.builder()
                    .setTitle(title)
                    .setBody(displayBody)
                    .build();

            Message message = Message.builder()
                    .setTopic(topic)
                    .setNotification(notification)
                    .setAndroidConfig(androidConfig)
                    .putAllData(data)
                    .build();


            FirebaseMessaging.getInstance().sendAsync(message);
            System.out.println("📡 DISPATCH ENGINE -> Outbound packet successfully handed to Google FCM for topic [" + topic + "]");

        } catch (Exception e) {
            System.err.println("❌ FCM Delivery Failure for topic [" + topic + "]: " + e.getMessage());
        }
    }
}