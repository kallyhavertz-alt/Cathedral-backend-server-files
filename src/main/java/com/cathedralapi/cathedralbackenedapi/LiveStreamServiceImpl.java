package com.cathedralapi.cathedralbackenedapi;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import java.time.LocalDateTime;
import java.util.List;

@Service
public class LiveStreamServiceImpl implements LiveStreamService {

    @Autowired
    private LiveStreamRepository repository;

    @Override
    public List<LiveStreamConfig> getAllActiveStreams() {
        return repository.findAllByOrderByUpdatedAtDesc();
    }

    @Override
    public LiveStreamConfig saveNewStream(LiveStreamConfig newConfig) {
        // Set current time stamp
        newConfig.setUpdatedAt(LocalDateTime.now());
        LiveStreamConfig saved = repository.save(newConfig);

        // 🚨 Capping Guardrail: If we have more than 4 items, delete the oldest
        List<LiveStreamConfig> allStreams = repository.findAllByOrderByUpdatedAtDesc();
        if (allStreams.size() > 4) {
            for (int i = 4; i < allStreams.size(); i++) {
                repository.delete(allStreams.get(i));
            }
        }
        return saved;
    }

    // ⏰ Runs automatically every day at midnight to sweep anything older than 3 days
    @Override
    @Scheduled(cron = "0 0 0 * * ?")
    public void purgeExpiredStreams() {
        LocalDateTime threeDaysAgo = LocalDateTime.now().minusDays(3);
        repository.deleteOldVideos(threeDaysAgo);
        System.out.println("🧼 Spring Boot Background Task: Cleared cathedral video streams older than 3 days.");
    }
}