package com.example.gateway.controller;

import com.example.gateway.dto.PaginationResponseDTO;
import com.example.gateway.service.HotelService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URISyntaxException;

@RestController
@RequestMapping("/api/v1")
public class HotelController {

    @Autowired
    private HotelService hotelService;

    @GetMapping(value = "/hotels")
    private ResponseEntity<?> getAllHotels(@RequestParam(value = "page", defaultValue = "0") int page,
                                           @RequestParam(value = "size", defaultValue = "10") int size) {
        try {
            PaginationResponseDTO response = this.hotelService.getHotels(page, size);
            return ResponseEntity.status(200).body(response);
        }catch (URISyntaxException e){
            System.out.println(" POGRESNA putanja");
            return ResponseEntity.status(400).build();
        }
    }

}
