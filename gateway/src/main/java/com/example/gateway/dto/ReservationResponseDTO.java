package com.example.gateway.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.HashMap;
import java.util.Map;

@Data
public class ReservationResponseDTO {
    private String reservationUid;
    private HotelInfoDTO hotel;
    private String startDate;
    private String endDate;
    private String status;

    private String paymentUid;

    private Map<String, Object> payment;

    public ReservationResponseDTO(){
        this.payment = new HashMap<>();
    }

    public ReservationResponseDTO(String reservationUid, HotelInfoDTO hotelInfoDTO, String startDate, String endDate, String status, String paymentUid, PaymentInfoDTO payment){
        this.reservationUid = reservationUid;
        this.hotel = hotelInfoDTO;
        this.startDate = startDate;
        this.endDate = endDate;
        this.status = status;
        this.paymentUid = paymentUid;
        this.payment.put("status", payment.getStatus());
        this.payment.put("price", payment.getPrice());
    }

    public void setPaymentUid(String paymentUid){
        this.paymentUid = paymentUid;
    }

    public void setPayment(PaymentInfoDTO p){
        if(p == null){
            this.payment = new HashMap<>();
        }
        this.payment.put("status", p.getStatus());
        this.payment.put("price", p.getPrice());
    }

}
