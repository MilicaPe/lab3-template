package com.example.payment.controller;

import com.example.payment.dto.PaymentInfoDTO;
import com.example.payment.model.Payment;
import com.example.payment.repository.PaymentRepository;
import com.example.payment.service.PaymentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping
public class PaymentController {
    @Autowired
    private PaymentRepository paymentRepository;

    @Autowired
    private PaymentService paymentService;

    @GetMapping(value = "/payment/{paymentUid}")
    public ResponseEntity<PaymentInfoDTO> getPaymentByPaymentUid(@PathVariable String paymentUid){
        Payment payment = this.paymentRepository.getPaymentByPaymentUid(UUID.fromString(paymentUid));
        return ResponseEntity.status(200).body(new PaymentInfoDTO(payment));
    }

    @PostMapping(value = "/payment")
    public ResponseEntity<String> saveNewPayment(@RequestBody PaymentInfoDTO paymentInfoDTO){
        UUID uuid = this.paymentService.saveNewPayment(paymentInfoDTO);
        return ResponseEntity.status(200).body(uuid.toString());
    }

    @DeleteMapping(value = "/payment/{paymentUid}")
    public ResponseEntity<?> saveNewPayment(@PathVariable String paymentUid){
        this.paymentService.deletePayment(paymentUid);
        return ResponseEntity.status(204).build();
    }

    }
