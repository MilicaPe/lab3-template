package com.example.gateway.dto;

import jakarta.annotation.sql.DataSourceDefinitions;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ReservationDTO {
    private String status;
    private String startDate;
    private String endDate;
    private String hotelUid;
    private String paymentUid;
}
