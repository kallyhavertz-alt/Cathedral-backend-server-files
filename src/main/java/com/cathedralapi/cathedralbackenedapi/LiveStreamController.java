package com.cathedralapi.cathedralbackenedapi;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/live")
@CrossOrigin(origins = "*")
public class LiveStreamController {

    @Autowired
    private LiveStreamService liveStreamService;

    // 🎯 Changed from a single object to a List of all active 3-4 videos
    @GetMapping("/current")
    public ResponseEntity<List<LiveStreamConfig>> getCurrentStreams() {
        return ResponseEntity.ok(liveStreamService.getAllActiveStreams());
    }

    // 🎯 Changed to POST since we are creating/appending a new record to our database history
    @PostMapping("/update")
    public ResponseEntity<LiveStreamConfig> addStream(@RequestBody LiveStreamConfig newConfig) {
        return ResponseEntity.ok(liveStreamService.saveNewStream(newConfig));
    }
}