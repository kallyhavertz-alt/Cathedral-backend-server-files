package com.cathedralapi.cathedralbackenedapi;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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
        System.out.println("🚀 SERVICE LAYER DEBUNKER: Saving note titled '" + note.getTitle() + "' for User ID: " + note.getUser().getId());

        return noteRepository.save(note);
    }

    public void updateFavoriteMetric(Long noteId, boolean updatedStatus) {
        Note note = noteRepository.findById(noteId)
                .orElseThrow(() -> new RuntimeException("Target note data element not found."));

        // Flip the boolean switch
        note.setIsFavorite(updatedStatus);

        // Commit to PostgreSQL
        noteRepository.save(note);
        System.out.println("⭐ CLOUD TRACKER: Note ID " + noteId + " favorite status set to: " + updatedStatus);

    }

    @Transactional
    public Note updateNote(Long noteId, NoteDto dto) {
        // 1. Fetch the original note row from PostgreSQL by its ID
        Note existingNote = noteRepository.findById(noteId)
                .orElseThrow(() -> new RuntimeException("Note row not found for ID: " + noteId));

        // 2. Overwrite the original fields with the new text strings coming from Flutter
        existingNote.setTitle(dto.getTitle());
        existingNote.setContent(dto.getContent());

        // Optional: If you update timestamps
        // existingNote.setUpdatedAt(LocalDateTime.now());

        // 3. Save the modified persistent record back to the PostgreSQL table
        return noteRepository.save(existingNote);
    }
    @Transactional
    public void deleteNoteById(Long noteId) {
        // Check existence first to avoid silent repository anomalies
        if (!noteRepository.existsById(noteId)) {
            throw new RuntimeException("Note isolation error: Target row not found for ID: " + noteId);
        }
        noteRepository.deleteById(noteId);
    }
}