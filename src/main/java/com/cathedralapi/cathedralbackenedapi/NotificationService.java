package com.cathedralapi.cathedralbackenedapi;

import com.cathedralapi.cathedralbackenedapi.Event;
import com.cathedralapi.cathedralbackenedapi.EventRepository;
import com.google.firebase.messaging.FirebaseMessaging;
import com.google.firebase.messaging.Message;
import com.google.firebase.messaging.Notification;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;




@Service
public class NotificationService {

    @Autowired
    private EventRepository eventRepository;

    /**
     * 📅 1. Pipeline for structural Event entities (Kept exactly as you designed)
     */
    public void sendPushNotification(Event event) {
        // 🛡️ Safety check: If a notification was already sent for this record, stop here to avoid spamming
        if ("true".equalsIgnoreCase(event.getNotificationSent())) {
            return;
        }

        try {
            System.out.println("📬 NotificationService triggered for Event ID: " + event.getId());
            System.out.println("📬 Raw UpdateType from Database entity: '" + event.getUpdateType() + "'");

            // Customize the notification title banner based on the Update Type
            String alertTitle = "📅 New Cathedral Update";
            if ("BISHOP_SPECIAL".equalsIgnoreCase(event.getUpdateType())) {
                alertTitle = "Bishop's Special Schedule Update";
            } else if ("EAGLES_LINK".equalsIgnoreCase(event.getUpdateType())) {
                alertTitle = "Eagles's Link Chapel Update";
            } else if ("ANNOUNCEMENT".equalsIgnoreCase(event.getUpdateType())) {
                alertTitle = "📢 New Announcement";
            } else if ("EVENT".equalsIgnoreCase(event.getUpdateType())) {
                alertTitle = "📅 Upcoming Church Event";
            }

            String alertBody = event.getEventTitle() + ": " + event.getDescription();

            // Build and send payload
            executeFcmTransmission(alertTitle, alertBody, event.getUpdateType());

            // 💾 Flip the flag to TRUE in PostgreSQL so this alert never triggers again
            event.setNotificationSent(String.valueOf(true));
            eventRepository.save(event);

        } catch (Exception e) {
            System.err.println("Failed to transmit Firebase Cloud Notification for Event: " + e.getMessage());
            e.printStackTrace();
        }
    }

    /**
     * 🔴 2. Generic Broadcaster (Used by LiveStreamServiceImpl!)
     * This matches the exact signature invoked in your live stream service engine.
     */
    public void sendBroadcastNotification(long entityId, String alertTitle, String alertBody, String updateType) {
        try {
            System.out.println("🚀 BROADCAST ENGINE: Launching FCM payload for Type: [" + updateType + "], ID: " + entityId);

            // Execute the raw Firebase send protocol
            executeFcmTransmission(alertTitle, alertBody, updateType);

        } catch (Exception e) {
            System.err.println("🚨 Broadcast Engine failed to deliver FCM message: " + e.getMessage());
            e.printStackTrace();
        }
    }

    /**
     * 📡 3. Private Helper: Code reuse to cleanly handle the heavy lifting for Firebase
     */
    private void executeFcmTransmission(String title, String body, String updateType) throws Exception {
        // Build the visible shade banner notification container
        Notification notification = Notification.builder()
                .setTitle(title)
                .setBody(body)
                .build();

        // Package the map payload parameters securely
        Message message = Message.builder()
                .putData("title", title)
                .putData("body", body)
                .setTopic("church_updates") // The global subscription topic channel
                .setNotification(notification)
                .putData("click_action", "FLUTTER_NOTIFICATION_CLICK") // Wakes Flutter app
                .putData("updateType", updateType)                     // Matches Flutter UI Image switcher case
                .build();

        // Fire transmission across the web to Firebase Servers
        String response = FirebaseMessaging.getInstance().send(message);
        System.out.println("✅ Firebase Server acknowledged receipt. Token signature: " + response);
    }
}