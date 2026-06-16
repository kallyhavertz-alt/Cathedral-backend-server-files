package com.cathedralapi.cathedralbackenedapi;

import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import com.cathedralapi.cathedralbackenedapi.NotificationService;
import org.springframework.stereotype.Service;
import java.time.LocalDateTime;
import java.util.List;

@Service
public class LiveStreamServiceImpl implements LiveStreamService {

    @Autowired
    private LiveStreamRepository repository;

    @Autowired
    private NotificationService notificationService;

    @Override
    public List<LiveStreamConfig> getAllActiveStreams() {

        return repository.findAllByOrderByUpdatedAtDesc();
    }
    @Override
    @Transactional // Good practice to ensure database updates and deletes wrap safely in a single transaction
    public LiveStreamConfig saveNewStream(LiveStreamConfig incomingConfig) {
        LiveStreamConfig configToSave;

        // 1. If it's an update, check the existing record state to see if a notification was already sent
        if (incomingConfig.getId() != null) {
            java.util.Optional<LiveStreamConfig> existing = repository.findById(incomingConfig.getId());
            if (existing.isPresent()) {
                configToSave = existing.get();
                configToSave.setTitle(incomingConfig.getTitle());
                configToSave.setStreamUrl(incomingConfig.getStreamUrl());
                configToSave.setLive(incomingConfig.isLive());
                // Keep the old notification state for evaluation unless the live toggle changed
            } else {
                configToSave = incomingConfig;
            }
        } else {
            configToSave = incomingConfig;
        }

        // 2. Update timestamp stamp
        configToSave.setUpdatedAt(LocalDateTime.now());

        // 3. 🚀 CRITICAL NOTIFICATION TRIGGER LOGIC (Clean as we did for events)
        if (configToSave.isLive() && !configToSave.isNotificationSent()) {

            try {
                // Blast FCM push payload out to the congregation
                notificationService.sendBroadcastNotification(
                        configToSave.getId() != null ? configToSave.getId() : 0L, // Handle temp id safety
                        "🔴 CATHEDRAL: LIVE NOW",
                        "We are live for: " + configToSave.getTitle(),
                        ""
                );

                // Flip the tracking permanently so updates don't spam Postman
                configToSave.setNotificationSent(true);
                System.out.println("📬 Live Link FCM broadcast successfully transmitted.");
            } catch (Exception e) {
                System.err.println("🚨 Notification pipeline dropped payload transmission: " + e.getMessage());
            }
        } else if (!configToSave.isLive()) {
            // 🔄 If an admin sets it to offline, reset the flag so it can fire fresh on the next stream service launch
            configToSave.setNotificationSent(false);
        }

        // 4. Commit to PostgreSQL
        LiveStreamConfig saved = repository.save(configToSave);

        // 🚨 Capping Guardrail: If we have more than 4 items, delete the oldest
        List<LiveStreamConfig> allStreams = repository.findAllByOrderByUpdatedAtDesc();
        if (allStreams.size() > 4) {
            for (int i = 4; i < allStreams.size(); i++) {
                repository.delete(allStreams.get(i));
            }
        }
        return saved;
    }

    // Runs automatically every day at midnight to sweep anything older than 3 days
    @Override
    @Transactional
    @Scheduled(cron = "0 0 0 * * ?")
    public void purgeExpiredStreams() {
        LocalDateTime threeDaysAgo = LocalDateTime.now().minusDays(3);
        repository.deleteOldVideos(threeDaysAgo);
        System.out.println("🧼 Spring Boot Background Task: Cleared cathedral video streams older than 3 days.");
    }
}