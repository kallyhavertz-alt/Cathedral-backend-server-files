package com.cathedralapi.cathedralbackenedapi;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<com.cathedralapi.cathedralbackenedapi.User, Long> {

    // Explicitly defining the path for the return type wrapper
    Optional<com.cathedralapi.cathedralbackenedapi.User> findByEmail(String email);
}