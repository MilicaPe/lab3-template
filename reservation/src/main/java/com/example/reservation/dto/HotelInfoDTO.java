package com.example.reservation.dto;

import com.example.reservation.model.Hotel;
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

    public HotelInfoDTO(Hotel hotel){
        this.hotelUid = hotel.getHotelUid().toString();
        this.name = hotel.getName();
        this.fullAddress = hotel.getCountry() + ", " + hotel.getCity() + ", " + hotel.getAddress();
        this.stars = hotel.getStars();
    }

}
