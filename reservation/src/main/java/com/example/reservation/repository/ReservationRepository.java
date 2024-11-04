package com.example.reservation.repository;

import com.example.reservation.model.Reservation;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface ReservationRepository extends JpaRepository<Reservation, Long> {
    List<Reservation> getAllByUsername(String username);

    Reservation getReservationByUsernameAndReservationUid(String username, UUID reservationUid);
}
