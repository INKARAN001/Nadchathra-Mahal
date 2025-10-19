package com.gsc.hallbooking.service;

import com.gsc.hallbooking.entity.Booking;
import com.gsc.hallbooking.entity.Payment;
import com.gsc.hallbooking.repository.PaymentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class PaymentService {
    
    private final PaymentRepository paymentRepository;
    
    @Transactional
    public Payment createPayment(Booking booking, String paymentMethod, String transactionId) {
        Payment payment = Payment.builder()
                .booking(booking)
                .amount(booking.getTotalAmount())
                .paymentMethod(paymentMethod)
                .transactionId(transactionId)
                .status("COMPLETED")
                .paymentDate(LocalDateTime.now())
                .build();
        
        return paymentRepository.save(payment);
    }
    
    @Transactional
    public Payment createPendingPayment(Booking booking, String paymentMethod) {
        Payment payment = Payment.builder()
                .booking(booking)
                .amount(booking.getTotalAmount())
                .paymentMethod(paymentMethod)
                .status("PENDING")
                .paymentDate(LocalDateTime.now())
                .build();
        
        return paymentRepository.save(payment);
    }
    
    @Transactional
    public void markPaymentAsCompleted(Long paymentId, String transactionId) {
        Payment payment = paymentRepository.findById(paymentId)
                .orElseThrow(() -> new RuntimeException("Payment not found"));
        payment.setStatus("COMPLETED");
        payment.setTransactionId(transactionId);
        payment.setPaymentDate(LocalDateTime.now());
        paymentRepository.save(payment);
    }
    
    @Transactional
    public void markPaymentAsFailed(Long paymentId) {
        Payment payment = paymentRepository.findById(paymentId)
                .orElseThrow(() -> new RuntimeException("Payment not found"));
        payment.setStatus("FAILED");
        paymentRepository.save(payment);
    }
    
    public Optional<Payment> getPaymentByBookingId(Long bookingId) {
        return paymentRepository.findByBookingId(bookingId);
    }
    
    public List<Payment> getAllPayments() {
        return paymentRepository.findAllByOrderByPaymentDateDesc();
    }
    
    public List<Payment> getPendingPayments() {
        return paymentRepository.findByStatus("PENDING");
    }
}

