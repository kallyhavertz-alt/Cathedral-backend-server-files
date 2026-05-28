package com.cathedralapi.cathedralbackenedapi;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface NoteRepository extends JpaRepository<Note, Long> {
    // Finds all personal notes written by a specific Cathedral member sorted by newest first
    List<Note> findByUserIdOrderByIdDesc(Long userId);
}