package com.cathedralapi.cathedralbackenedapi;

import com.cathedralapi.cathedralbackenedapi.NoticeDTO;
import com.cathedralapi.cathedralbackenedapi.PostDTO;
import com.cathedralapi.cathedralbackenedapi.CathedralPost;
import com.cathedralapi.cathedralbackenedapi.StaffNotice;
import com.cathedralapi.cathedralbackenedapi.CathedralPostRepository;
import com.cathedralapi.cathedralbackenedapi.StaffNoticeRepository;
import com.cathedralapi.cathedralbackenedapi.CathedralPortalService;
import com.cathedralapi.cathedralbackenedapi.NotificationDispatchService;
import org.springframework.stereotype.Service;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class CathedralPortalServiceImpl implements CathedralPortalService {

    private final CathedralPostRepository postRepository;
    private final StaffNoticeRepository noticeRepository;
    private final NotificationDispatchService notificationService;

    private final DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
    private final DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("hh:mm a");

    public CathedralPortalServiceImpl(CathedralPostRepository postRepository,
                                      StaffNoticeRepository noticeRepository,
                                      NotificationDispatchService notificationService) {
        this.postRepository = postRepository;
        this.noticeRepository = noticeRepository;
        this.notificationService = notificationService;
    }

    @Override
    public PostDTO createPost(PostDTO dto) {
        CathedralPost post = new CathedralPost();
        post.setPostType(dto.getPostType());
        post.setSubService(dto.getSubService() != null ? dto.getSubService() : "NONE");
        post.setTitle(dto.getTitle());
        post.setContent(dto.getContent());
        post.setSenderId(dto.getSenderId()); // 🚀 NEW: Capture sender identifier on storage

        CathedralPost saved = postRepository.save(post);

        // 🚀 CRITICAL PRIORITY: Trigger live automated notification immediately
        notificationService.dispatchPostNotification(saved);

        return mapToPostDTO(saved);
    }

    @Override
    public NoticeDTO createNotice(NoticeDTO dto) {
        StaffNotice notice = new StaffNotice();
        notice.setContent(dto.getContent());
        notice.setSenderId(dto.getSenderId()); // 🚀 NEW: Capture sender identifier on storage

        StaffNotice saved = noticeRepository.save(notice);

        // 🚀 CRITICAL PRIORITY: Trigger private staff push instantly
        notificationService.dispatchStaffNoticeNotification(saved);

        return mapToNoticeDTO(saved);
    }

    @Override
    public List<PostDTO> getPostsByCategory(String category) {
        return postRepository.findByPostTypeOrderByCreatedAtDesc(category)
                .stream().map(this::mapToPostDTO).collect(Collectors.toList());
    }

    @Override
    public List<PostDTO> getAllPostsHistory() {
        return postRepository.findAllByOrderByCreatedAtDesc()
                .stream().map(this::mapToPostDTO).collect(Collectors.toList());
    }

    @Override
    public List<NoticeDTO> getAllNoticesHistory() {
        return noticeRepository.findAllByOrderByCreatedAtDesc()
                .stream().map(this::mapToNoticeDTO).collect(Collectors.toList());
    }

    // 🚀 NEW: Isolated retrieval methods tracking sender identity
    @Override
    public List<PostDTO> getPostsBySender(String senderId) {
        return postRepository.findAllBySenderIdOrderByCreatedAtDesc(senderId)
                .stream().map(this::mapToPostDTO).collect(Collectors.toList());
    }

    @Override
    public List<NoticeDTO> getNoticesBySender(String senderId) {
        return noticeRepository.findAllBySenderIdOrderByCreatedAtDesc(senderId)
                .stream().map(this::mapToNoticeDTO).collect(Collectors.toList());
    }

    private PostDTO mapToPostDTO(CathedralPost post) {
        PostDTO dto = new PostDTO();
        dto.setId(post.getId());
        dto.setPostType(post.getPostType());
        dto.setSubService(post.getSubService());
        dto.setTitle(post.getTitle());
        dto.setContent(post.getContent());
        dto.setSenderId(post.getSenderId()); // Map sender data downstream
        // 🧠 Split the native timestamp cleanly on the backend before sending to Flutter
        dto.setFormattedDate(post.getCreatedAt().format(dateFormatter));
        dto.setFormattedTime(post.getCreatedAt().format(timeFormatter));
        return dto;
    }

    private NoticeDTO mapToNoticeDTO(StaffNotice notice) {
        NoticeDTO dto = new NoticeDTO();
        dto.setId(notice.getId());
        dto.setContent(notice.getContent());
        dto.setSenderId(notice.getSenderId()); // Map sender data downstream
        dto.setFormattedDate(notice.getCreatedAt().format(dateFormatter));
        dto.setFormattedTime(notice.getCreatedAt().format(timeFormatter));
        return dto;
    }
}