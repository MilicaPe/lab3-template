package com.example.reservation.dto;

import com.example.reservation.model.Hotel;
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

    public HotelResponseDTO(Hotel hotel){
        this.hotelUid = hotel.getHotelUid().toString();
        this.name = hotel.getName();
        this.country = hotel.getCountry();
        this.city = hotel.getCity();
        this.address = hotel.getAddress();
        this.stars = hotel.getStars();
        this.price = hotel.getPrice();
    }
}
