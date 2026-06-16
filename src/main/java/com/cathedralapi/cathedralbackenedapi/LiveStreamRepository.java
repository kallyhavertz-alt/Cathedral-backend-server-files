package com.cathedralapi.cathedralbackenedapi;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;
import java.time.LocalDateTime;
import java.util.List;

public interface LiveStreamRepository extends JpaRepository<LiveStreamConfig, Long> {

    // Fetches videos ordered by newest first
    List<LiveStreamConfig> findAllByOrderByUpdatedAtDesc();

    @Modifying
    @Query("DELETE FROM LiveStreamConfig v WHERE v.updatedAt < :cutoffTime")
    void deleteOldVideos(LocalDateTime cutoffTime);
}
