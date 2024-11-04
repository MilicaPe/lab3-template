package com.example.gateway.controller;


import com.example.gateway.dto.CreateReservationRequest;
import com.example.gateway.dto.CreateReservationResponse;
import com.example.gateway.dto.ReservationResponseDTO;
import com.example.gateway.dto.error.ErrorResponse;
import com.example.gateway.dto.error.ValidationErrorResponse;
import com.example.gateway.service.ReservationService;
import com.example.gateway.service.ReservationServiceLogic;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URISyntaxException;

@RestController
@RequestMapping("/api/v1")
public class ReservationController {

    @Autowired
    ReservationServiceLogic reservationServiceLogic;

    @Autowired
    ReservationService reservationService;

    @GetMapping(value = "/reservations/{reservationUid}")
    public ResponseEntity<?> getUserReservationsByUid (@RequestHeader("X-User-Name") String username, @PathVariable String reservationUid) throws URISyntaxException {
        try {
            ReservationResponseDTO result = this.reservationService.getUserReservationsByUid(username, reservationUid);
            return ResponseEntity.status(200).body(result);
        }catch (ErrorResponse e) {
            return ResponseEntity.status(404).body(e);
        }
    }

    @PostMapping(value="/reservations")
    public ResponseEntity makeNewReservation(@RequestHeader("X-User-Name") String username, @RequestBody CreateReservationRequest request) throws URISyntaxException {
        try {
            CreateReservationResponse response = this.reservationServiceLogic.makeReservation(username, request);
            return ResponseEntity.status(200).body(response);
        }catch (ValidationErrorResponse e){
            return ResponseEntity.status(400).body(e);
        }
    }

    @DeleteMapping(value = "/reservations/{reservationUid}")
    public ResponseEntity<?> deleteReservation(@RequestHeader("X-User-Name") String username, @PathVariable String reservationUid) throws URISyntaxException {
        try {
            this.reservationServiceLogic.deleteReservation(username, reservationUid);
            return ResponseEntity.status(204).build();
        } catch (ErrorResponse e) {
            return ResponseEntity.status(404).body(e);
        }

    }
}
