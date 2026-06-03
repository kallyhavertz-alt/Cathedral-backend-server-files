package com.cathedralapi.cathedralbackenedapi;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/notes")
@CrossOrigin(origins = "*")
public class NoteController {

    @Autowired
    private NoteService noteService; // 🛡️ Service layer injected, replacing raw repositories

    // 🔍 1. FETCH ALL NOTES FOR A SPECIFIC USER
    @GetMapping("/user/{userId}")
    public ResponseEntity<List<Note>> getAllNotes(@PathVariable("userId") Long userId) {
        List<Note> notes = noteService.getNotesByUserId(userId);
        return ResponseEntity.ok(notes);
    }

    // 💾 2. CREATE A VERY NEW NOTE (Using the secure DTO wrapper)
    @PostMapping("/add/{userId}")
    public ResponseEntity<?> createNote(@PathVariable("userId") Long userId, @RequestBody NoteDto dto) {
        try {
            Note saved = noteService.createNote(userId, dto);
            return ResponseEntity.ok(saved);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    // 📝 3. UPDATE AN EXISTING NOTE
    @PutMapping("/update/{noteId}")
    public ResponseEntity<?> updateNote(@PathVariable("noteId") Long noteId, @RequestBody Map<String, String> updates) {
        try {
            Note updated = noteService.createNote(noteId, (NoteDto) updates);
            return ResponseEntity.ok(updated);
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }
}