package com.cathedralapi.cathedralbackenedapi;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface EventRepository extends JpaRepository<Event, Long> {
    // Fetches all events sorted by upcoming date sequence
    List<Event> findAllByOrderByEventDateAsc();
    List<Event> findByUpdateType(String UpdateType);

    List<Event> findByNotificationSentFalse();
}
