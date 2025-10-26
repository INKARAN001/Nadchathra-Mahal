package com.gsc.hallbooking.repository;

import com.gsc.hallbooking.entity.LoginInfo;
import com.gsc.hallbooking.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface LoginInfoRepository extends JpaRepository<LoginInfo, Long> {
    List<LoginInfo> findByUser(User user);
    List<LoginInfo> findByUserId(Long userId);
    List<LoginInfo> findByLoginDate(LocalDate loginDate);
    List<LoginInfo> findByUserOrderByLoginDateDescLoginTimeDesc(User user);
    List<LoginInfo> findByUserIdOrderByLoginDateDescLoginTimeDesc(Long userId);
    List<LoginInfo> findAllByOrderByLoginDateDescLoginTimeDesc();
    
    // Get recent logins
    List<LoginInfo> findTop10ByUserIdOrderByLoginDateDescLoginTimeDesc(Long userId);
}

