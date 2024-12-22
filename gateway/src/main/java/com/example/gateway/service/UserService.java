package com.example.gateway.service;

import com.example.gateway.dto.*;
import com.example.gateway.dto.error.LoyaltyServiceException;
import com.example.gateway.dto.error.PaymentServiceException;
import com.example.gateway.dto.error.ReservationServiceException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;

@Service
public class UserService {

    @Value("${reservation.service.url}")
    private String basicReservation;


    @Autowired
    PaymentService paymentService;

    @Autowired
    LoyaltyService loyaltyService;

    @Autowired
    ReservationService reservationService;

    public UserInfoResponseDTO getUserInfo(String username) throws URISyntaxException, ReservationServiceException {
        System.out.println(" Stigao na gateway");
        List<ReservationResponseDTO> reservations = getReservationsDTOForUser(username);
        LoyaltyInfoResponseDTO loyalty;
        try {
            loyalty = loyaltyService.getLoyaltyForUser(username);
            return new UserInfoResponseDTO(reservations, loyalty);
        } catch (LoyaltyServiceException e) {
            return new UserInfoResponseDTO(reservations);
        }
    }

    public List<ReservationResponseDTO> getReservationsDTOForUser(String username) throws URISyntaxException, ReservationServiceException {
        List<ReservationResponseDTO> reservations = reservationService.getReservationsForUser(username);
        this.setPaymentForReservations(reservations);
        return reservations;
    }



    private void setPaymentForReservations(List<ReservationResponseDTO> reservations) throws URISyntaxException {
        for(ReservationResponseDTO r: reservations){
            try {
                r.setPayment(paymentService.getPaymentInfo(r.getPaymentUid()));
            }catch (PaymentServiceException e){
                r.setPayment(null);
            }
        }
    }

}
