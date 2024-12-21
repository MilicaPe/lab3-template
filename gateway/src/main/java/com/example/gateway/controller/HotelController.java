package com.example.gateway.controller;

import com.example.gateway.dto.PaginationResponseDTO;
import com.example.gateway.dto.error.ErrorResponse;
import com.example.gateway.dto.error.ReservationServiceException;
import com.example.gateway.service.ReservationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URISyntaxException;

@RestController
@RequestMapping("/api/v1")
public class HotelController {

    @Autowired
    private ReservationService reservationService;

    @GetMapping(value = "/hotels")
    private ResponseEntity<?> getAllHotels(@RequestParam(value = "page", defaultValue = "0") int page,
                                           @RequestParam(value = "size", defaultValue = "10") int size) {
        try {
            PaginationResponseDTO response = this.reservationService.getHotels(page, size);
            return ResponseEntity.status(200).body(response);
        }catch (ReservationServiceException e){
            return ResponseEntity.status(503).body(new ErrorResponse("Reservation Service unavailable"));
        }catch (URISyntaxException e){
            System.out.println(" POGRESNA putanja");
            return ResponseEntity.status(503).build();
        }
    }

}
