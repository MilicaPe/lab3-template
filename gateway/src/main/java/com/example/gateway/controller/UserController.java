package com.example.gateway.controller;

import com.example.gateway.dto.LoyaltyInfoResponseDTO;
import com.example.gateway.dto.ReservationResponseDTO;
import com.example.gateway.dto.UserInfoResponseDTO;
import com.example.gateway.service.LoyaltyService;
import com.example.gateway.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URISyntaxException;
import java.util.List;

@RestController
@RequestMapping("/api/v1")
public class UserController {

    @Autowired
    private UserService userService;

    @Autowired
    private LoyaltyService loyaltyService;

    @GetMapping(value = "/me")
    public ResponseEntity<UserInfoResponseDTO> getUserInfo(@RequestHeader("X-User-Name") String username) throws URISyntaxException {
        UserInfoResponseDTO response = this.userService.getUserInfo(username);
        return ResponseEntity.status(200).body(response);

    }

    @GetMapping(value = "/loyalty")
    public ResponseEntity<LoyaltyInfoResponseDTO> getLoyaltyInfo(@RequestHeader("X-User-Name") String username) throws URISyntaxException {
        LoyaltyInfoResponseDTO loyalty = this.loyaltyService.getLoyaltyForUser(username);
        return ResponseEntity.status(200).body(loyalty);
    }

    @GetMapping(value = "/reservations")
    public ResponseEntity<List<ReservationResponseDTO>> getUserReservations (@RequestHeader("X-User-Name") String username) throws URISyntaxException {
        List<ReservationResponseDTO> reservationsDTO = this.userService.getReservationsDTOForUser(username);
        return ResponseEntity.status(200).body(reservationsDTO);
    }


}
