package com.example.gateway.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PaginationResponseDTO {
    private int page;
    private int pageSize;
    private int totalElements;
    private List<HotelResponseDTO> items;
}
