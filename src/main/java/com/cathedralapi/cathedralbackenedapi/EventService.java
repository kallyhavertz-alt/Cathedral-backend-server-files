package com.cathedralapi.cathedralbackenedapi;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import java.util.List;

@Service // 🎯 This tells Spring Boot this is a service bean so @Autowired works!
public class EventService {

    @Autowired
    private EventRepository eventRepository;
    // Your existing repository

    // 🎯 The method your controller is looking for to save data
    // Inside your EventService or EventController where you fetch events
    List<Event> events = eventRepository.findAll(Sort.by(Sort.Direction.DESC, "id"));
    public Event saveEvent(Event event) {
        return eventRepository.save(event);
    }

    // Optional: Useful later when Flutter needs to pull the updates
    public List<Event> getAllEvents() {

        return eventRepository.findAllByOrderByIdDesc();
    }
}
