package com.example.gateway.service;

import com.example.gateway.dto.*;
import com.example.gateway.dto.error.ErrorResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.net.URI;
import java.net.URISyntaxException;

@Service
public class ReservationService {
    @Value("${reservation.service.url}")
    private String basicReservation;

    @Autowired
    private PaymentService paymentService;


    public ReservationResponseDTO getUserReservationsByUid(String username, String reservationUid) throws URISyntaxException, ErrorResponse {
        URI uri = new URI(this.basicReservation.toString() + "/reservation/"+reservationUid);
        System.out.println(uri.toString());
        RestTemplate restTemplate = new RestTemplate();
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity entity = new HttpEntity(headers);
        headers.add("X-User-Name", username);
        ResponseEntity<ReservationResponseDTO> result = restTemplate.exchange(uri, HttpMethod.GET, entity, ReservationResponseDTO.class);
        try {
            ReservationResponseDTO r = (ReservationResponseDTO) result.getBody();
            r.setPayment(this.paymentService.getPaymentInfo(r.getPaymentUid()));
            return r;
        }catch (Exception e){
            throw new ErrorResponse("Reservation desnt exist");
        }
    }


    public CreateReservationResponse saveReservation(ReservationDTO reservationDTO, String username) throws URISyntaxException {
        URI uri = new URI(this.basicReservation.toString() + "/reservation");
        System.out.println(uri.toString());
        RestTemplate restTemplate = new RestTemplate();
        HttpHeaders headers = new HttpHeaders();
        headers.add("X-User-Name", username);
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity entity = new HttpEntity(reservationDTO, headers);
        ResponseEntity<CreateReservationResponse> result = restTemplate.exchange(uri, HttpMethod.POST, entity, CreateReservationResponse.class);
        return result.getBody();
    }

    public ReservationDTO deleteReservationForUser(String username, String reservationUid) throws URISyntaxException {
        URI uri = new URI(this.basicReservation.toString() + "/reservation/" + reservationUid);
        System.out.println(uri.toString());
        RestTemplate restTemplate = new RestTemplate();
        HttpHeaders headers = new HttpHeaders();
        headers.add("X-User-Name", username);
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity entity = new HttpEntity(headers);
        ResponseEntity<ReservationDTO> result = restTemplate.exchange(uri, HttpMethod.DELETE, entity, ReservationDTO.class);
        System.out.println(result.getBody());
        return result.getBody();
    }
}
