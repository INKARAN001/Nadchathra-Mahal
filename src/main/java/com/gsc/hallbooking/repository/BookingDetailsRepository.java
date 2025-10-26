package com.gsc.hallbooking.repository;

import com.gsc.hallbooking.entity.BookingDetails;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface BookingDetailsRepository extends JpaRepository<BookingDetails, Long> {
    Optional<BookingDetails> findByBookingId(Long bookingId);
}

