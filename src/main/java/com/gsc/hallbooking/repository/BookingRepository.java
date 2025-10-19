package com.gsc.hallbooking.repository;

import com.gsc.hallbooking.entity.Booking;
import com.gsc.hallbooking.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface BookingRepository extends JpaRepository<Booking, Long> {
    List<Booking> findByUser(User user);
    List<Booking> findByUserOrderByIdDesc(User user);
    List<Booking> findAllByOrderByIdDesc();
    boolean existsByHallAndBookingDate(String hall, LocalDate bookingDate);
}

