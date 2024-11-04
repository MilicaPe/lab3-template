package com.example.gateway.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class HotelInfoDTO {
    private String hotelUid;
    private String name;
    private String fullAddress;
    private int stars;
}
