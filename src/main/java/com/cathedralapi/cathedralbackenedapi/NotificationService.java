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

    public void sendPushNotification(Event event) {
        // 🛡️ Safety check: If a notification was already sent for this record, stop here to avoid spamming
        if (event.isNotificationSent()) {
            return;
        }

        try {
            // 🎯 1. Customize the notification title banner based on the Update Type
            String alertTitle = "New Cathedral Update";
            if ("BISHOP_SPECIAL".equalsIgnoreCase(event.getUpdateType())) {
                alertTitle = "Bishop's Special Schedule Update";
            } else if ("ANNOUNCEMENT".equalsIgnoreCase(event.getUpdateType())) {
                alertTitle = "📢 New Announcement";
            } else if ("EVENT".equalsIgnoreCase(event.getUpdateType())) {
                alertTitle = "📅 Upcoming Church Event";
            }

                String alertBody = event.getEventTitle() + ": " + event.getDescription();


            // 📝 2. Build the visual text payload for the phone shade
            Notification notification = Notification.builder()
                    .setTitle(alertTitle)
                    .setBody(alertBody)
                    .build();

            // 📡 3. Package the message to broadcast to everyone subscribed to "church_updates"
            Message message = Message.builder()
                    .putData("title", alertTitle)
                    .putData("body", alertBody)
                    .setTopic("church_updates") // Broadcast channel
                    .setNotification(notification)
                    .putData("click_action", "FLUTTER_NOTIFICATION_CLICK") // Tells Flutter to open the app on click
                    .putData("updateType", event.getUpdateType())       // Passes data variables quietly to the app
                    .build();

            // 🚀 4. Blast it out via Firebase Cloud Messaging (FCM)
            String response = FirebaseMessaging.getInstance().send(message);
            System.out.println("Successfully sent push notification payload: " + response);

            // 💾 5. Flip the flag to TRUE in PostgreSQL so this alert never triggers again
            event.setNotificationSent(true);
            eventRepository.save(event);

        } catch (Exception e) {
            System.err.println("Failed to transmit Firebase Cloud Notification: " + e.getMessage());
            e.printStackTrace();
        }
    }
}
