package com.cathedralapi.cathedralbackenedapi;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/api/notes")
@CrossOrigin(origins = "*")
public class NoteController {

    @Autowired
    private NoteRepository noteRepository;

    // 🎯 ADDED: Injected UserRepository to find the real user object from the DB
    @Autowired
    private UserRepository userRepository;

    // 🔍 1. FETCH ALL NOTES FOR A SPECIFIC USER (Ordered by newest first)
    @GetMapping("/user/{userId}")
    public ResponseEntity<List<Note>> getAllNotes(@PathVariable("userId") Long userId) {
        List<Note> notes = noteRepository.findByUserIdOrderByIdDesc(userId);
        return ResponseEntity.ok(notes);
    }

    // 💾 2. CREATE A BRAND NOTE (Linked securely to the verified User Object)
    @PostMapping("/add/{userId}")
    public ResponseEntity<?> createNote(@PathVariable("userId") Long userId, @RequestBody Note noteDetails) {
        try {
            // 1. Look up the parent user entity or throw a clean error if missing
            User user = userRepository.findById(userId)
                    .orElseThrow(() -> new RuntimeException("User not found with ID: " + userId));

            Note note = new Note();

            // 2. Map data fields from your Flutter application request package
            note.setEventId(noteDetails.getEventId());
            note.setEventTitle(noteDetails.getEventTitle());
            note.setTitle(noteDetails.getTitle());
            note.setContent(noteDetails.getContent());

            // 🎯 THE FIX: Attach the real managed entity relation here directly
            note.setUser(user);

            Note saved = noteRepository.save(note);
            return ResponseEntity.ok(saved);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Error saving note: " + e.getMessage());
        }
    }

    // 📝 3. UPDATE AN EXISTING NOTE (From the Workspace Editor Screen)
    @PutMapping("/update/{noteId}")
    public ResponseEntity<?> updateNote(@PathVariable("noteId") Long noteId, @RequestBody Map<String, String> updates) {
        Optional<Note> noteOpt = noteRepository.findById(noteId);

        if (noteOpt.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        Note existingNote = noteOpt.get();

        // Only update if the field is present in the request body map
        if (updates.containsKey("title")) {
            existingNote.setTitle(updates.get("title"));
        }
        if (updates.containsKey("content")) {
            existingNote.setContent(updates.get("content"));
        }

        Note updated = noteRepository.save(existingNote);
        return ResponseEntity.ok(updated);
    }
}