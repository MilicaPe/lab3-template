package com.example.loyalty.dto;

import com.example.loyalty.model.Loyalty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class LoyaltyInfoResponseDTO {
    private String status;
    private int discount;
    private int reservationCount;

    public LoyaltyInfoResponseDTO(Loyalty loyalty){
        this.status = loyalty.getStatus().toString();
        this.discount = loyalty.getDiscount();
        this.reservationCount = loyalty.getReservationCount();
    }

}
