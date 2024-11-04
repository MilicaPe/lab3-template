package com.example.loyalty.controller;

import com.example.loyalty.dto.LoyaltyInfoResponseDTO;
import com.example.loyalty.model.Loyalty;
import com.example.loyalty.repository.LoyaltyRepository;
import com.example.loyalty.service.LoyaltyService;
import jakarta.websocket.server.PathParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping
public class LoyaltyController {
    @Autowired
    private LoyaltyRepository loyaltyRepository;
    @Autowired
    private LoyaltyService loyaltyService;

    @GetMapping(value = "/loyalty")
    public ResponseEntity<LoyaltyInfoResponseDTO> getLoyaltyInfoResponseForUser(@RequestHeader("X-User-Name")  String username){
        System.out.println(" korisnik za loyalty " + username);
        Loyalty loyalty = this.loyaltyRepository.getLoyaltyByUsername(username);
        System.out.println(loyalty.getStatus() + " " + loyalty.getDiscount());
        return ResponseEntity.status(200).body(new LoyaltyInfoResponseDTO(loyalty));
    }

    @PostMapping(value = "/loyalty")
    public ResponseEntity<LoyaltyInfoResponseDTO> addNewBooking(@RequestHeader("X-User-Name") String username){
        Loyalty loyalty = this.loyaltyService.addNewBooking(username);
        return ResponseEntity.status(200).body(new LoyaltyInfoResponseDTO(loyalty));
    }

    @DeleteMapping(value = "/loyalty")
    public ResponseEntity<LoyaltyInfoResponseDTO> subtractBooking(@RequestHeader("X-User-Name") String username){
        Loyalty loyalty = this.loyaltyService.subtractBooking(username);
        return ResponseEntity.status(200).body(new LoyaltyInfoResponseDTO(loyalty));
    }
}
