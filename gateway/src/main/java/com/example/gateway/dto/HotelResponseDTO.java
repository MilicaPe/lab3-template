package com.example.gateway.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class HotelResponseDTO {
    private String  hotelUid;
    private String name;
    private String country;
    private String city;
    private String address;
    private int stars;
    private double price;
}
