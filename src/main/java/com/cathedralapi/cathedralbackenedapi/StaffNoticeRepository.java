package com.cathedralapi.cathedralbackenedapi;

import com.cathedralapi.cathedralbackenedapi.StaffNotice;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface StaffNoticeRepository extends JpaRepository<StaffNotice, Long> {
    List<StaffNotice> findAllByOrderByCreatedAtDesc();

    List<StaffNotice> findAllBySenderIdOrderByCreatedAtDesc(String senderId);
}