package com.example.gateway.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserInfoResponseDTO {
    private List<ReservationResponseDTO> reservations;
    //@JsonInclude(JsonInclude.Include.NON_NULL)
    private Map<String, Object> loyalty;

//    public UserInfoResponseDTO(List<ReservationResponseDTO> reservations) {
//        this.reservations = reservations;
//    }

    public UserInfoResponseDTO (List<ReservationResponseDTO> reservations){
        this.reservations = reservations;
        this.loyalty = new HashMap<>();
    }

    public UserInfoResponseDTO(List<ReservationResponseDTO> reservations, LoyaltyInfoResponseDTO loyalty){
        this.reservations = reservations;
        this.loyalty = new HashMap<>();
        this.loyalty.put("status", loyalty.getStatus());
        this.loyalty.put("discount", loyalty.getDiscount());
        this.loyalty.put("reservationCount", loyalty.getReservationCount());
    }
}
