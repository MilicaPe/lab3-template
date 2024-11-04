package com.example.reservation;

import com.example.reservation.model.Hotel;
import com.example.reservation.repository.HotelRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.util.ArrayList;
import java.util.UUID;

@SpringBootApplication
public class ReservationApplication {
	@Autowired
	static
	HotelRepository hotelRepository;

	public static void main(String[] args) {
		SpringApplication.run(ReservationApplication.class, args);
//		Hotel h = new Hotel(1L, UUID.fromString("049161bb-badd-4fa8-9d90-87c9a82b0668"), "Ararat Park Hyatt Moscow", "Россия", "Москва", "Неглинная ул., 4", 5, 10000.0, new ArrayList<>());
//		hotelRepository.save(h);
	}

}
