package com.example.reservation.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ReservationDTO {
    private String status;
    private String startDate;
    private String endDate;
    private String hotelUid;
    private String paymentUid;
}