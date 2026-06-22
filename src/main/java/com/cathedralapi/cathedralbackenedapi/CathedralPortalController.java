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

    // 📥 FETCH FEED CONTENT (For General App Tabs - public view stays open)
    @GetMapping("/posts")
    public ResponseEntity<List<PostDTO>> getPostsByCategory(@RequestParam String type) {
        return ResponseEntity.ok(portalService.getPostsByCategory(type));
    }

    // 📥 FETCH ISOLATED SENT POSTS HISTORY (Filtered by active staff identity)
    @GetMapping("/staff/history/posts")
    public ResponseEntity<List<PostDTO>> getAllPostsHistory(@RequestParam(required = false) String senderId) {
        if (senderId != null && !senderId.trim().isEmpty()) {
            return ResponseEntity.ok(portalService.getPostsBySender(senderId));
        }
        return ResponseEntity.ok(portalService.getAllPostsHistory());
    }

    // 📥 FETCH ISOLATED SENT NOTICES HISTORY (Filtered by active staff identity)
    @GetMapping("/staff/history/notices")
    public ResponseEntity<List<NoticeDTO>> getAllNoticesHistory(@RequestParam(required = false) String senderId) {
        if (senderId != null && !senderId.trim().isEmpty()) {
            return ResponseEntity.ok(portalService.getNoticesBySender(senderId));
        }
        return ResponseEntity.ok(portalService.getAllNoticesHistory());
    }
}