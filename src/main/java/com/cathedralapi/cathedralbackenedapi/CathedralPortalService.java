package com.cathedralapi.cathedralbackenedapi;

import com.cathedralapi.cathedralbackenedapi.NoticeDTO;
import com.cathedralapi.cathedralbackenedapi.PostDTO;
import java.util.List;

public interface CathedralPortalService {
    PostDTO createPost(PostDTO postDTO);
    NoticeDTO createNotice(NoticeDTO noticeDTO);
    List<PostDTO> getPostsByCategory(String category);
    List<PostDTO> getAllPostsHistory();
    List<NoticeDTO> getAllNoticesHistory();
}