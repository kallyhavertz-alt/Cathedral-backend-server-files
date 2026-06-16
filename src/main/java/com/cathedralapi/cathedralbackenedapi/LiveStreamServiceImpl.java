package com.cathedralapi.cathedralbackenedapi;

import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import com.cathedralapi.cathedralbackenedapi.NotificationService;
import org.springframework.stereotype.Service;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

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
    @Transactional
    public LiveStreamConfig saveNewStream(LiveStreamConfig incomingConfig) {
        LiveStreamConfig configToSave;

        // 1. Check for existing update records
        if (incomingConfig.getId() != null) {
            Optional<LiveStreamConfig> existing = repository.findById(incomingConfig.getId());
            if (existing.isPresent()) {
                configToSave = existing.get();
                configToSave.setTitle(incomingConfig.getTitle());
                configToSave.setStreamUrl(incomingConfig.getStreamUrl());

                // 🚀 FIX 1: Changed incomingConfig.isLive() -> incomingConfig.getIsLive()
                configToSave.setIsLive(incomingConfig.getIsLive());
            } else {
                configToSave = incomingConfig;
            }
        } else {
            configToSave = incomingConfig;
        }

        configToSave.setUpdatedAt(LocalDateTime.now());

        // 🚀 THE OBJECT WRAPPER SAFETY PARSER ENGINE
        // We safely treat null values from legacy database columns as false
        boolean liveStatus = configToSave.getIsLive() != null && configToSave.getIsLive(); // 🚀 FIX 2: Changed .isLive() -> .getIsLive()
        boolean alertAlreadySent = configToSave.getNotificationSent() != null && configToSave.getNotificationSent(); // 🚀 FIX 3: Changed .isNotificationSent() -> .getNotificationSent()

        // 2. Evaluate condition to trigger notification broadcast
        if (liveStatus && !alertAlreadySent) {
            try {
                notificationService.sendBroadcastNotification(
                        configToSave.getId() != null ? configToSave.getId() : 0L,
                        "🔴 EAGLES LINK: LIVE NOW",
                        "We are live for: " + configToSave.getTitle(),
                        "EAGLES_LINK"
                );

                configToSave.setNotificationSent(true);
                System.out.println("📬 Live Link FCM broadcast successfully transmitted.");
            } catch (Exception e) {
                System.err.println("🚨 Notification pipeline dropped payload transmission: " + e.getMessage());
            }
        } else if (!liveStatus) { // 🚀 FIX 4: Evaluates using our clean primitive boolean variable
            configToSave.setNotificationSent(false);
        }

        // 3. Commit to PostgreSQL securely
        LiveStreamConfig saved = repository.save(configToSave);

        // Capping Guardrail (Keeps table size minimal)
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