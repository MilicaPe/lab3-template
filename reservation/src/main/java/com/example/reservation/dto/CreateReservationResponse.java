package com.example.reservation.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CreateReservationResponse {
    private String reservationUid;
    private String hotelUid;
    private String startDate;
    private String endDate;
    private int discount;
    private String status;
//    private PaymentInfoDTO payment;
}
