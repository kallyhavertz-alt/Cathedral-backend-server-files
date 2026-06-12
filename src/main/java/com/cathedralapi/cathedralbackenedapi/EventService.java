package com.cathedralapi.cathedralbackenedapi;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import java.util.List;

@Service // 🎯 This tells Spring Boot this is a service bean so @Autowired works!
public class EventService {

    @Autowired
    private EventRepository eventRepository;
    public Event saveEvent(Event event) {
        return eventRepository.save(event);
    }
    public List<Event> getAllEvents() {

        return eventRepository.findAllByOrderByIdDesc();
    }
}
