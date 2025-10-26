package com.gsc.hallbooking.repository;

import com.gsc.hallbooking.entity.Admin;
import com.gsc.hallbooking.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface AdminRepository extends JpaRepository<Admin, Long> {
    Optional<Admin> findByUser(User user);
    Optional<Admin> findByUserId(Long userId);
    List<Admin> findByUserStatus(String userStatus);
    List<Admin> findAllByOrderByTotalBookingsDesc();
    boolean existsByUserId(Long userId);
}

