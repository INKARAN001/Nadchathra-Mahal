package com.gsc.hallbooking.repository;

import com.gsc.hallbooking.entity.Booking;
import com.gsc.hallbooking.entity.Payment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface PaymentRepository extends JpaRepository<Payment, Long> {
    Optional<Payment> findByBooking(Booking booking);
    Optional<Payment> findByBookingId(Long bookingId);
    List<Payment> findByStatus(String status);
    List<Payment> findAllByOrderByPaymentDateDesc();
}

