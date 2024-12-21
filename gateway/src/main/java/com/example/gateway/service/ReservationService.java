package com.example.gateway.service;

import com.example.gateway.circuit.breaker.CircuitBreaker;
import com.example.gateway.dto.*;
import com.example.gateway.dto.error.ErrorResponse;
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
import java.time.Duration;
import java.util.ArrayList;
import java.util.List;

@Service
public class ReservationService {
    @Value("${reservation.service.url}")
    private String basicUrl;

    @Autowired
    private PaymentService paymentService;

    @Autowired
    CircuitBreaker reservationCircuitBreaker;


    public ReservationResponseDTO getUserReservationsByUid(String username, String reservationUid) throws URISyntaxException, ErrorResponse, ReservationServiceException {
        if(!this.reservationCircuitBreaker.allowRequest()) {
            this.reservationCircuitBreaker.recordFailure();
            throw new ReservationServiceException();
        }
        try {
            URI uri = new URI(this.basicUrl.toString() + "/reservation/" + reservationUid);
            System.out.println(uri.toString());
            RestTemplate restTemplate = new RestTemplate();
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);
            HttpEntity entity = new HttpEntity(headers);
            headers.add("X-User-Name", username);
            ResponseEntity<ReservationResponseDTO> result = restTemplate.exchange(uri, HttpMethod.GET, entity, ReservationResponseDTO.class);
            ReservationResponseDTO r = null;
            try {
                r = result.getBody();
                r.setPayment(this.paymentService.getPaymentInfo(r.getPaymentUid()));
            } catch (PaymentServiceException e) {
                r.setPayment(null);
            } catch (Exception e) {
                throw new ErrorResponse("Reservation desnt exist");
            }
            this.reservationCircuitBreaker.recordSuccess();
            return r;
        }catch (Exception e){
            this.reservationCircuitBreaker.recordFailure();
            throw new ReservationServiceException();
        }
    }


    public CreateReservationResponse saveReservation(ReservationDTO reservationDTO, String username) throws URISyntaxException, ReservationServiceException {
        if(!this.reservationCircuitBreaker.allowRequest()) {
            this.reservationCircuitBreaker.recordFailure();
            throw new ReservationServiceException();
        }
        try {
            URI uri = new URI(this.basicUrl.toString() + "/reservation");
            System.out.println(uri.toString());
            RestTemplate restTemplate = new RestTemplate();
            HttpHeaders headers = new HttpHeaders();
            headers.add("X-User-Name", username);
            headers.setContentType(MediaType.APPLICATION_JSON);
            HttpEntity entity = new HttpEntity(reservationDTO, headers);
            ResponseEntity<CreateReservationResponse> result = restTemplate.exchange(uri, HttpMethod.POST, entity, CreateReservationResponse.class);
            this.reservationCircuitBreaker.recordSuccess();
            return result.getBody();
        }catch (Exception e){
            this.reservationCircuitBreaker.recordFailure();
            throw new ReservationServiceException();
        }
    }

    public ReservationDTO deleteReservationForUser(String username, String reservationUid) throws URISyntaxException {
        URI uri = new URI(this.basicUrl.toString() + "/reservation/" + reservationUid);
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


    public List<ReservationResponseDTO> getReservationsForUser(String username) throws URISyntaxException, ReservationServiceException {
        if(!this.reservationCircuitBreaker.allowRequest()) {
            this.reservationCircuitBreaker.recordFailure();
            throw new ReservationServiceException();
        }
        try {
            URI uri = new URI(this.basicUrl.toString() + "/reservation");      // +username);
            System.out.println(uri.toString());
            RestTemplate restTemplate = new RestTemplate();
            HttpHeaders headers = new HttpHeaders();
            headers.add("X-User-Name", username);
            headers.setContentType(MediaType.APPLICATION_JSON);
            HttpEntity entity = new HttpEntity(headers);
            ResponseEntity<ArrayList<ReservationResponseDTO>> result = restTemplate.exchange(
                    uri,
                    HttpMethod.GET,
                    entity,
                    new ParameterizedTypeReference<>() {
                    });
            System.out.println(result.getBody());
            System.out.println(result);
            this.reservationCircuitBreaker.recordSuccess();
            return result.getBody();
        }catch (Exception e){
            this.reservationCircuitBreaker.recordFailure();
            throw new ReservationServiceException();
        }
    }

    public PaginationResponseDTO getHotels(int page, int row) throws URISyntaxException, ReservationServiceException {
        if(!this.reservationCircuitBreaker.allowRequest()) {
            this.reservationCircuitBreaker.recordFailure();
            throw new ReservationServiceException();
        }
        try {
            System.out.println(" Stigao na gateway");
            URI uri = new URI(this.basicUrl.toString() + "/hotels/" + page + "/" + row);
            System.out.println(uri.toString());
            RestTemplate restTemplate = new RestTemplate();
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);
            HttpEntity entity = new HttpEntity(headers);
            ResponseEntity<PaginationResponseDTO> result = restTemplate.exchange(uri, HttpMethod.GET, entity, PaginationResponseDTO.class);
            System.out.println(result.getBody());
            System.out.println(result);
            if (result.getStatusCode().value() == 500) {
                this.reservationCircuitBreaker.recordFailure();
                throw new ReservationServiceException();
            }
            this.reservationCircuitBreaker.recordSuccess();
            return result.getBody();
        } catch (Exception e){
            this.reservationCircuitBreaker.recordFailure();
            throw new ReservationServiceException();
        }
    }

    public Boolean isHotelExist(String hotelUid) throws URISyntaxException, ReservationServiceException {
        if(!this.reservationCircuitBreaker.allowRequest()) {
            this.reservationCircuitBreaker.recordFailure();
            throw new ReservationServiceException();
        }
        try {
            URI uri = new URI(this.basicUrl.toString() + "/hotel/" + hotelUid);
            System.out.println(uri.toString());
            RestTemplate restTemplate = new RestTemplate();
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);
            HttpEntity entity = new HttpEntity(headers);
            ResponseEntity<Boolean> result = restTemplate.exchange(uri, HttpMethod.GET, entity, Boolean.class);
            System.out.println("HOTEL : " + result.getBody());
            this.reservationCircuitBreaker.recordSuccess();
            return result.getBody();
        } catch (Exception e){
            this.reservationCircuitBreaker.recordFailure();
            throw new ReservationServiceException();
        }
    }

    public double getHotelPrice(String hotelUid) throws URISyntaxException, ReservationServiceException {
        if(!this.reservationCircuitBreaker.allowRequest()) {
            this.reservationCircuitBreaker.recordFailure();
            throw new ReservationServiceException();
        }
        try {
            URI uri = new URI(this.basicUrl.toString() + "/hotel/price/" + hotelUid);
            System.out.println(uri.toString());
            RestTemplate restTemplate = new RestTemplate();
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);
            HttpEntity entity = new HttpEntity(headers);
            ResponseEntity<Double> result = restTemplate.exchange(uri, HttpMethod.GET, entity, Double.class);
            this.reservationCircuitBreaker.recordSuccess();
            return result.getBody();
        }catch (Exception e){
            this.reservationCircuitBreaker.recordFailure();
            throw new ReservationServiceException();
        }
    }
}
