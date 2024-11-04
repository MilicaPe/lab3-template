package com.example.reservation.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ReservationResponseDTO {
    private String reservationUid;
    private HotelInfoDTO hotel;
    private String startDate;
    private String endDate;
    private String status;

    private String paymentUid;

}
