package com.example.payment.dto;

import com.example.payment.model.Payment;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PaymentInfoDTO {
    private String status;
    private int price;

    public PaymentInfoDTO(Payment payment){
        this.status = payment.getStatus().toString();
        this.price = payment.getPrice();
    }
}
