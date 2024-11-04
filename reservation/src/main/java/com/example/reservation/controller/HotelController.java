package com.example.reservation.controller;

import com.example.reservation.dto.HotelResponseDTO;
import com.example.reservation.dto.PaginationResponseDTO;
import com.example.reservation.model.Hotel;
import com.example.reservation.repository.HotelRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.*;

@RestController
@RequestMapping
public class HotelController {

    @Autowired
    private HotelRepository hotelRepository;

    @GetMapping(value = "/hotels/{page}/{row}")
    public ResponseEntity<PaginationResponseDTO> getAllHotels(@PathVariable int page, @PathVariable int row){
        System.out.println("Stigeo na reservation service");
        List<Hotel> hotels = this.hotelRepository.findAll(PageRequest.of(setPage(page), row)).getContent();
        PaginationResponseDTO paginationResponseDTO = getHotelsPagination(page, row, hotels);
        return ResponseEntity.status(200).body(paginationResponseDTO);
    }

    private PaginationResponseDTO getHotelsPagination(int page, int row, List<Hotel> hotels){
        List<HotelResponseDTO> hotelResponseDTOS = new ArrayList<>();
        for(Hotel hotel: hotels){
            hotelResponseDTOS.add(new HotelResponseDTO(hotel));
        }
        return new PaginationResponseDTO(page, row, hotelResponseDTOS.size(), hotelResponseDTOS);
    }

    @GetMapping(value = "/hotel/{hotelUid}")
    public ResponseEntity<Boolean> isHotelConsist(@PathVariable String hotelUid){
        Hotel hotel = this.hotelRepository.findHotelByHotelUid(UUID.fromString(hotelUid));
        if (hotel == null){
            return ResponseEntity.status(404).body(false);
        }else{
            return ResponseEntity.status(200).body(true);
        }
    }

    @GetMapping(value = "/hotel/price/{hotelUid}")
    public ResponseEntity<Object> getHotelPrice(@PathVariable String hotelUid){
        Hotel hotel = this.hotelRepository.findHotelByHotelUid(UUID.fromString(hotelUid));
        if (hotel == null){
            return ResponseEntity.status(404).body(null);
        }else{
            return ResponseEntity.status(200).body(hotel.getPrice());
        }
    }

    private int setPage(int page){
        if (page == 0){
            return 0;
        }else{
            return page-1;
        }
    }



}
