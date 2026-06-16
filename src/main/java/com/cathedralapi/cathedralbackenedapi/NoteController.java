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

    //  FETCH ALL NOTES FOR A SPECIFIC USER
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

    //  UPDATE AN EXISTING NOTE
    @PutMapping("/update/{noteId}")
    public ResponseEntity<?> updateNote(@PathVariable("noteId") Long noteId, @RequestBody NoteDto dto) {
        try {
            // Pass the target primary key ID and the type-safe DTO payload to a dedicated update handler
            Note updated = noteService.updateNote(noteId, dto);
            return ResponseEntity.ok(updated);
        } catch (RuntimeException e) {
            System.err.println("🚨 BACKEND UPDATE FAILURE: " + e.getMessage());
            return ResponseEntity.status(500).body("Failed to update note: " + e.getMessage());
        }
    }
    @PatchMapping("/{noteId}/favorite")
    public ResponseEntity<?> toggleNoteFavoriteStatus(
            @PathVariable Long noteId,
            @RequestParam("status") boolean targetStatus) {
        try {
            // Pass the incoming value directly! No inversion needed.
            noteService.updateFavoriteMetric(noteId, targetStatus);
            return ResponseEntity.ok().build();
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
    //  PERMANENTLY DELETE A NOTE
    @DeleteMapping("/delete/{noteId}")
    public ResponseEntity<?> deleteNotePermanently(@PathVariable("noteId") Long noteId) {
        System.out.println("📡 DELETION NETWORK HIT: Request to permanently purge Note ID: " + noteId);
        try {
            noteService.deleteNoteById(noteId);
            System.out.println("✅ BACKEND PURGE SUCCESS: Note ID " + noteId + " removed from PostgreSQL.");
            return ResponseEntity.ok().body("Note deleted successfully");
        } catch (RuntimeException e) {
            System.err.println("🚨 BACKEND DELETION CRASH: Could not drop note row: " + e.getMessage());
            return ResponseEntity.status(404).body(e.getMessage());
        }
    }
}