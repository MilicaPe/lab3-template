package com.example.reservation.service;

import com.example.reservation.dto.*;
import com.example.reservation.model.Hotel;
import com.example.reservation.model.Reservation;
import com.example.reservation.repository.HotelRepository;
import com.example.reservation.repository.ReservationRepository;
import org.hibernate.ObjectNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
public class ReservationService {
    @Autowired
    private ReservationRepository reservationRepository;

    @Autowired
    private HotelRepository hotelRepository;



    public List<ReservationResponseDTO> getReservationsByUser(String username) {
        List<ReservationResponseDTO> responseDTOS = new ArrayList<>();
        List<Reservation> reservations = this.reservationRepository.getAllByUsername(username);
        for(Reservation r : reservations){
            HotelInfoDTO hotelInfoDTO = new HotelInfoDTO(r.getHotel());
            responseDTOS.add(new ReservationResponseDTO(r.getReservationUid().toString(),
                    hotelInfoDTO, r.getStartDate().toString().split("T")[0], r.getEndDate().toString().split("T")[0], r.getStatus().toString(),
                    r.getPaymentUid().toString()));
        }
        return responseDTOS;
    }

    public ReservationResponseDTO getReservationByUser(String reservationUid, String username) {
        Reservation r = this.reservationRepository.getReservationByUsernameAndReservationUid(username, UUID.fromString(reservationUid));
        if (r==null){
         throw new ObjectNotFoundException("Not Found", new ErrorResponse());
        }
        else {
            HotelInfoDTO hotelInfoDTO = new HotelInfoDTO(r.getHotel());
            ReservationResponseDTO responseDTO = new ReservationResponseDTO(r.getReservationUid().toString(),
                    hotelInfoDTO, r.getStartDate().toString().split("T")[0], r.getEndDate().toString().split("T")[0], r.getStatus(),
                    r.getPaymentUid().toString());
            return responseDTO;
        }
    }

    public CreateReservationResponse saveNewReservation(String username, ReservationDTO reservationDTO) {
        Hotel hotel = hotelRepository.findHotelByHotelUid(UUID.fromString(reservationDTO.getHotelUid()));
        Reservation reservation = new Reservation();
        reservation.setUsername(username);
        reservation.setHotel(hotel);
        reservation.setStatus(reservationDTO.getStatus());
        reservation.setPaymentUid(UUID.fromString(reservationDTO.getPaymentUid()));
        reservation.setStartDate(OffsetDateTime.parse(reservationDTO.getStartDate()+"T12:00:00+03:00"));
        reservation.setEndDate(OffsetDateTime.parse(reservationDTO.getEndDate()+"T12:00:00+03:00"));
        reservation.setReservationUid(UUID.randomUUID());

        Reservation saved = this.reservationRepository.save(reservation);
        return formCreateReservationResponse(saved);
    }

    private CreateReservationResponse formCreateReservationResponse(Reservation reservation){
        CreateReservationResponse response = new CreateReservationResponse();
        response.setReservationUid(reservation.getReservationUid().toString());
        response.setStartDate(reservation.getStartDate().toLocalDate().toString());
        System.out.println(reservation.getStartDate().toLocalDate().toString().split("T")[0]);

        response.setEndDate(reservation.getEndDate().toString().split("T")[0]);
        response.setHotelUid(reservation.getHotel().getHotelUid().toString());
        response.setStatus(reservation.getStatus().toString());
        return response;
    }

    public ReservationDTO deleteReservation(String username, String reservationUid) {
        Reservation r = this.reservationRepository.getReservationByUsernameAndReservationUid(username, UUID.fromString(reservationUid));
        if (r==null){
            System.out.println("nema takve rezervacije");
            return null;
        }
        r.setStatus("CANCELED");
        Reservation updated = this.reservationRepository.save(r);
        return new ReservationDTO(updated.getStatus().toString(),
                updated.getStartDate().toString(),
                updated.getEndDate().toString(),
                updated.getHotel().getHotelUid().toString(),
                updated.getPaymentUid().toString());
    }
}
