package com.example.gateway.controller;

import com.example.gateway.circuit.breaker.CircuitBreaker;
import com.example.gateway.dto.LoyaltyInfoResponseDTO;
import com.example.gateway.dto.ReservationResponseDTO;
import com.example.gateway.dto.UserInfoResponseDTO;
import com.example.gateway.dto.error.ErrorResponse;
import com.example.gateway.dto.error.LoyaltyServiceException;
import com.example.gateway.dto.error.ReservationServiceException;
import com.example.gateway.service.LoyaltyService;
import com.example.gateway.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URISyntaxException;
import java.time.Duration;
import java.util.List;

@RestController
@RequestMapping("/api/v1")
public class UserController {

    @Autowired
    private UserService userService;

    @Autowired
    private LoyaltyService loyaltyService;

    @GetMapping(value = "/me")
    public ResponseEntity<?> getUserInfo(@RequestHeader("X-User-Name") String username) throws URISyntaxException {
        try {
            UserInfoResponseDTO response = this.userService.getUserInfo(username);
            return ResponseEntity.status(200).body(response);
        }catch (ReservationServiceException e){
            return ResponseEntity.status(503).body(new ErrorResponse("Reservation Service unavailable"));
        }
    }

    @GetMapping(value = "/loyalty", produces = "application/json")
    public ResponseEntity<?> getLoyaltyInfo(@RequestHeader("X-User-Name") String username) throws URISyntaxException {
        try {
            LoyaltyInfoResponseDTO loyalty = this.loyaltyService.getLoyaltyForUser(username);
            return ResponseEntity.status(200).body(loyalty);
        } catch (LoyaltyServiceException e){
            return ResponseEntity.status(503).body(new ErrorResponse("Loyalty Service unavailable"));
        }
    }

    @GetMapping(value = "/reservations")
    public ResponseEntity<?> getUserReservations (@RequestHeader("X-User-Name") String username) throws URISyntaxException {
        try {
            List<ReservationResponseDTO> reservationsDTO = this.userService.getReservationsDTOForUser(username);
            return ResponseEntity.status(200).body(reservationsDTO);
        }catch (ReservationServiceException e){
            return ResponseEntity.status(503).body(new ErrorResponse("Reservation Service unavailable"));
        }
    }


}
