package com.example.reservation.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.OffsetDateTime;
import java.util.UUID;

@Entity
@Table
@AllArgsConstructor
@NoArgsConstructor
@Data
public class Reservation {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true, nullable = false)
    private UUID reservationUid;

    @Column(nullable = false)
    private String username;

    @Column(unique = true, nullable = false)
    private UUID paymentUid;

    @ManyToOne
    @JoinColumn(name="hotel_id")
    private Hotel hotel;

    @Column(nullable = false)
    private String status;

    @Column(name = "start_date")
    private OffsetDateTime startDate;

    @Column(name = "end_data")
    private OffsetDateTime endDate;

}
