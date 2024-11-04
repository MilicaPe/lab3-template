package com.example.reservation.controller;

import com.example.reservation.dto.CreateReservationResponse;
import com.example.reservation.dto.ReservationDTO;
import com.example.reservation.dto.ReservationResponseDTO;
import com.example.reservation.service.ReservationService;
import org.hibernate.ObjectNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping
public class ReservationController {
    @Autowired
    private ReservationService reservationService;

    @GetMapping(value = "/reservation")
    public ResponseEntity<List<ReservationResponseDTO>> getAllReservationsForUser(@RequestHeader("X-User-Name") String username){
        List<ReservationResponseDTO> result = this.reservationService.getReservationsByUser(username);
        return ResponseEntity.status(200).body(result);
    }

    @GetMapping(value = "/reservation/{reservationUid}")
    public ResponseEntity <ReservationResponseDTO> getReservationForUser(@PathVariable String reservationUid, @RequestHeader("X-User-Name") String username){
        try {
            ReservationResponseDTO result = this.reservationService.getReservationByUser(reservationUid, username);
            return ResponseEntity.status(200).body(result);
        }catch (ObjectNotFoundException e){
            return  ResponseEntity.status(404).body(null);
        }

    }

    @PostMapping(value = "/reservation")
    public ResponseEntity<CreateReservationResponse> saveNewReservation(@RequestHeader("X-User-Name") String username, @RequestBody ReservationDTO reservationDTO){
        CreateReservationResponse response = this.reservationService.saveNewReservation(username, reservationDTO);
        return ResponseEntity.status(200).body(response);
    }

    @DeleteMapping(value = "/reservation/{reservationUid}")
    public ResponseEntity<ReservationDTO> deleteReservation(@RequestHeader("X-User-Name") String username, @PathVariable String reservationUid){
        ReservationDTO reservation = reservationService.deleteReservation(username, reservationUid);
        if (reservation == null){
            return ResponseEntity.status(404).body(null);
        }else{
            System.out.println("reyervacija: " + reservation.getStatus().toString());
            return ResponseEntity.status(200).body(reservation);
        }
    }
}
