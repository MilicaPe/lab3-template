package com.example.gateway.service;

import com.example.gateway.dto.*;
import com.example.gateway.dto.error.ErrorResponse;
import com.example.gateway.dto.error.ValidationErrorResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.net.URISyntaxException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;

@Service
public class ReservationServiceLogic {
    @Autowired
    private PaymentService paymentService;
    @Autowired
    private HotelService hotelService;

    @Autowired
    private LoyaltyService loyaltyService;

    @Autowired
    private ReservationService reservationService;

    private final String paid = "PAID";
    private final String canceled = "CANCELED";



    public CreateReservationResponse makeReservation(String username, CreateReservationRequest request) throws URISyntaxException, ValidationErrorResponse {
        validateReservationRequest(request);
        int numOfNights = countNumberOfNights(request);
        Double pricePerNight = hotelService.getHotelPrice(request.getHotelUid());
        double fullPrice = numOfNights * pricePerNight;
        LoyaltyInfoResponseDTO loyalty = loyaltyService.getLoyaltyForUser(username);
        int discount = loyaltyService.getDiscountForStatus(loyalty.getStatus());
        int endPrice = (int) (fullPrice - (fullPrice * discount / 100));
        PaymentInfoDTO newPaymentInfo = new PaymentInfoDTO(paid, endPrice);
        String paymentUid = this.paymentService.saveNewPayment(newPaymentInfo);

        ReservationDTO reservationDTO = new ReservationDTO();
        reservationDTO.setPaymentUid(paymentUid);
        reservationDTO.setHotelUid(request.getHotelUid());
        reservationDTO.setStartDate(request.getStartDate());
        reservationDTO.setEndDate(request.getEndDate());
        reservationDTO.setStatus(paid);

        CreateReservationResponse response = this.reservationService.saveReservation(reservationDTO, username);
        response.setPayment(newPaymentInfo);
        response.setDiscount(discount);

        LoyaltyInfoResponseDTO loyaltyInfo = loyaltyService.addNewBooking(username);

        return response;
    }
    private int countNumberOfNights(CreateReservationRequest request){
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        LocalDate startDate = LocalDate.parse(request.getStartDate(), formatter);
        LocalDate endDate = LocalDate.parse(request.getEndDate(), formatter);
        int numOfNights = (int) ChronoUnit.DAYS.between(startDate, endDate);
        return numOfNights;
    }

    private boolean validateReservationRequest(CreateReservationRequest request) throws URISyntaxException, ValidationErrorResponse {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        LocalDate startDate = LocalDate.parse(request.getStartDate(), formatter);
        LocalDate endDate = LocalDate.parse(request.getEndDate(), formatter);
        LocalDate now = LocalDate.now();
        if ((startDate.isBefore(now)) || (endDate.isBefore(now)))
            throw new ValidationErrorResponse("Dates are in the past", new ArrayList<>());
        if (endDate.isBefore(startDate)){
            throw new ValidationErrorResponse("End date is before start date", new ArrayList<>());
        }
        if (!hotelService.isHotelExist(request.getHotelUid()))
            throw new ValidationErrorResponse("Hotel is not exist", new ArrayList<>());
        return true;
    }

    public boolean deleteReservation(String username, String reservationUid) throws URISyntaxException, ErrorResponse {
        ReservationDTO r = reservationService.deleteReservationForUser(username, reservationUid);
        if (r == null || r.getStatus().equals("PAID")){
            System.out.println("reservation " + r);
            throw new ErrorResponse("Reservation doesnt exist");
        }
        this.paymentService.deletePayment(r.getPaymentUid());
        this.loyaltyService.subtractBooking(username);
        return true;
    }


}
