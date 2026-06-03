package com.cathedralapi.cathedralbackenedapi;

import com.cathedralapi.cathedralbackenedapi.Event;
import com.cathedralapi.cathedralbackenedapi.EventService;
import com.cathedralapi.cathedralbackenedapi.NotificationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/events")
public class EventController {

    @Autowired
    private EventService eventService;

    @Autowired
    private NotificationService notificationService;

    // 🔥 FIXED: Now calling the lowercase instance variable via the Service layer
    @GetMapping("/all")
    public List<Event> getAllEvents() {
        return eventService.getAllEvents();
    }

    @PostMapping("/save")
    public ResponseEntity<Event> createEvent(@RequestBody Event event) {
        Event savedEvent = eventService.saveEvent(event);
        notificationService.sendPushNotification(savedEvent);
        return ResponseEntity.ok(savedEvent);
    }
}