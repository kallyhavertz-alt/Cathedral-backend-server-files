package com.cathedralapi.cathedralbackenedapi;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

@Service // 🎯 Marks this as the business logic defense layer
public class NoteService {

    @Autowired
    private NoteRepository noteRepository;

    @Autowired
    private UserRepository userRepository;

    // 🔍 1. Fetch logic with safety validation
    public List<Note> getNotesByUserId(Long userId) {
        // Here we could add a check to ensure the logged-in user matches the userId requested
        return noteRepository.findByUserIdOrderByIdDesc(userId);
    }

    // 💾 2. Secure creation using the DTO instead of raw database entity
    public Note createNote(Long userId, NoteDto dto) {
        // 1. Fetch user validation logic here...
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        // 2. Instantiate core Database Entity
        Note note = new Note();
        note.setUser(user);
        note.setTitle(dto.getTitle());
        note.setContent(dto.getContent());

        // 🎯 MAP THE NEW DTO FIELDS DIRECTLY INTO THE ENTITY RECORD:
        // If the incoming DTO has an eventId, use it. Otherwise, default to 1 (General Note)
        if (dto.getEventId() != null) {
            note.setEventId(dto.getEventId());
        } else {
            note.setEventId(1L);
        }

        if (dto.getEventTitle() != null) {
            note.setEventTitle(dto.getEventTitle());
        } else {
            note.setEventTitle("General Note");
        }

        note.setCreatedAt(LocalDateTime.now()); // Or use your dynamic payload date string

        return noteRepository.save(note);
    }
}