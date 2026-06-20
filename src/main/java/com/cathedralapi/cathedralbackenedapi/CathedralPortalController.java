package com.cathedralapi.cathedralbackenedapi;

import com.cathedralapi.cathedralbackenedapi.NoticeDTO;
import com.cathedralapi.cathedralbackenedapi.PostDTO;
import com.cathedralapi.cathedralbackenedapi.CathedralPortalService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/v1")
@CrossOrigin(origins = "*") // Allows effortless Flutter connectivity without CORS errors
public class CathedralPortalController {

    private final CathedralPortalService portalService;

    public CathedralPortalController(CathedralPortalService portalService) {
        this.portalService = portalService;
    }

    // 📤 PUSH POST FROM STAFF WORKSPACE
    @PostMapping("/staff/create-post")
    public ResponseEntity<PostDTO> createPost(@RequestBody PostDTO postDTO) {
        return ResponseEntity.ok(portalService.createPost(postDTO));
    }

    // 📤 PUSH NOTICE FROM STAFF WORKSPACE
    @PostMapping("/staff/create-notice")
    public ResponseEntity<NoticeDTO> createNotice(@RequestBody NoticeDTO noticeDTO) {
        return ResponseEntity.ok(portalService.createNotice(noticeDTO));
    }

    // 📥 FETCH FEED CONTENT (For General App Tabs)
    @GetMapping("/posts")
    public ResponseEntity<List<PostDTO>> getPostsByCategory(@RequestParam String type) {
        return ResponseEntity.ok(portalService.getPostsByCategory(type));
    }

    // 📥 FETCH ALL SENT POSTS HISTORY (For Workspace History Feed)
    @GetMapping("/staff/history/posts")
    public ResponseEntity<List<PostDTO>> getAllPostsHistory() {
        return ResponseEntity.ok(portalService.getAllPostsHistory());
    }

    // 📥 FETCH ALL SENT NOTICES HISTORY (For Workspace History Feed)
    @GetMapping("/staff/history/notices")
    public ResponseEntity<List<NoticeDTO>> getAllNoticesHistory() {
        return ResponseEntity.ok(portalService.getAllNoticesHistory());
    }
}
