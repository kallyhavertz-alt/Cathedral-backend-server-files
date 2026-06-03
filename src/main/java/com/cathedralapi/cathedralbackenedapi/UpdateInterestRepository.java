package com.cathedralapi.cathedralbackenedapi;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface UpdateInterestRepository extends JpaRepository<UpdateInterest, Long> {

    // 🛡️ Explicitly checks if a row exists matching this user ID value
    @Query(value = "SELECT EXISTS(SELECT 1 FROM update_interests WHERE user_id = :userId)", nativeQuery = true)
    boolean hasThisUserSubmitted(@Param("userId") Long userId);
}