package com.cathedralapi.cathedralbackenedapi;

import com.cathedralapi.cathedralbackenedapi.CathedralPost;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface CathedralPostRepository extends JpaRepository<CathedralPost, Long> {
    // 🔍 Dynamic chronological search query fetching items newest first
    List<CathedralPost> findByPostTypeOrderByCreatedAtDesc(String postType);
    List<CathedralPost> findAllByOrderByCreatedAtDesc();
}