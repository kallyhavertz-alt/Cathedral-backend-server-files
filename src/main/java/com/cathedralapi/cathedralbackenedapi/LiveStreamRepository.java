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

    // 🧼 The custom query to delete videos older than 3 days
    @Transactional
    @Modifying
    @Query("DELETE FROM LiveStreamConfig v WHERE v.updatedAt < :cutoffTime")
    void deleteOldVideos(LocalDateTime cutoffTime);
}
/*package com.cathedralapi.cathedralbackenedapi;

import com.cathedralapi.cathedralbackenedapi.LiveStreamConfig;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface LiveStreamRepository extends JpaRepository<LiveStreamConfig, Long> {
    // We just need to grab the latest configuration row
    LiveStreamConfig findFirstByOrderByIdAsc();
}*/