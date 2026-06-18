package com.cathedralapi.cathedralbackenedapi;

import com.cathedralapi.cathedralbackenedapi.StaffEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.Optional;

@Repository
public interface StaffRepository extends JpaRepository<StaffEntity, Long> {
    Optional<StaffEntity> findByEmail(String email);

    Optional<StaffEntity> findByPhoneNumber(String phoneNumber);

    Optional<StaffEntity> findByEmailOrPhoneNumber(String email, String phoneNumber);
}
