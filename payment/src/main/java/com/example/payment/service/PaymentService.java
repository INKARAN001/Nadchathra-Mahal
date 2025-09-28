package com.example.payment.service;

import com.example.payment.dto.PaymentRequestDTO;
import com.example.payment.dto.PaymentResponseDTO;
import com.example.payment.entity.PaymentTransaction;
import com.example.payment.respository.PaymentTransactionRepository;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class PaymentService {

    private final PaymentTransactionRepository repository;

    public PaymentService(PaymentTransactionRepository repository) {
        this.repository = repository;
    }

    public PaymentResponseDTO validateAndSave(PaymentRequestDTO dto) {
        if (!dto.getCardNumber().matches("\\d{16}")) {
            return new PaymentResponseDTO("Invalid card number! Must be 16 digits.", "FAILED");
        }
        if (!dto.getCvv().matches("\\d{3}")) {
            return new PaymentResponseDTO("Invalid CVV! Must be 3 digits.", "FAILED");
        }
        if (dto.getExpiryMonth() == null || !dto.getExpiryMonth().matches("0[1-9]|1[0-2]")) {
            return new PaymentResponseDTO("Invalid expiry month!", "FAILED");
        }
        if (dto.getExpiryYear() == null || !dto.getExpiryYear().matches("\\d{2}")) {
            return new PaymentResponseDTO("Invalid expiry year!", "FAILED");
        }

        PaymentTransaction tx = new PaymentTransaction();
        tx.setName(dto.getName());
        tx.setEventName(dto.getEventName());
        tx.setCardNumber(dto.getCardNumber());
        tx.setExpiryMonth(dto.getExpiryMonth());
        tx.setExpiryYear(dto.getExpiryYear());
        tx.setCvv(dto.getCvv());
        tx.setStatus(PaymentTransaction.PaymentStatus.COMPLETED);

        PaymentTransaction savedTx = repository.save(tx);

        return new PaymentResponseDTO(
                "Card validated successfully for " + dto.getEventName(),
                "COMPLETED",
                savedTx.getId()
        );
    }

    public List<PaymentTransaction> findAll() {
        return repository.findAll();
    }
}
