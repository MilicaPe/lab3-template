package com.example.payment.service;

import com.example.payment.dto.PaymentInfoDTO;
import com.example.payment.model.Payment;
import com.example.payment.repository.PaymentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class PaymentService {
    @Autowired
    private PaymentRepository paymentRepository;

    public UUID saveNewPayment(PaymentInfoDTO paymentInfoDTO){
        UUID uuid = UUID.randomUUID();
        Payment payment = new Payment();
        payment.setPaymentUid(uuid);
        payment.setPrice(paymentInfoDTO.getPrice());
        payment.setStatus(paymentInfoDTO.getStatus());
        Payment saved = this.paymentRepository.save(payment);
        return saved.getPaymentUid();
    }

    public void deletePayment(String paymentUid) {
        Payment payment = this.paymentRepository.getPaymentByPaymentUid(UUID.fromString(paymentUid));
        payment.setStatus("CANCELED");
        this.paymentRepository.save(payment);
    }
}
