package com.example.gateway;

import com.example.gateway.dto.*;
import com.example.gateway.dto.error.*;
import com.example.gateway.service.*;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.context.SpringBootTest;

import java.net.URISyntaxException;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@SpringBootTest
@ExtendWith(MockitoExtension.class)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class ReservationServiceTest {

    @Mock
    private PaymentService paymentService;
    @Mock
    private LoyaltyService loyaltyService;

    @Mock
    private ReservationService reservationService;

    @InjectMocks
    private ReservationServiceLogic reservationServiceLogic;

    private CreateReservationRequest request;
    private CreateReservationRequest requestStartEndDate;
    private CreateReservationRequest requestDatesInPast;
    private LoyaltyInfoResponseDTO loyalty;
    private PaymentInfoDTO payment;

    private String paymentUid;
    private String reservationUid;
    private String hotelUid;
    private String startDate;

    private String endDate;
    private int discount;
    private String username;
    private int price;
    private String loyaltyStatus;
    private CreateReservationResponse response;

    private LoyaltyInfoResponseDTO loyaltyInfo;
    private ReservationDTO reservationDTO;
    private ReservationDTO reservationDTOdelete;

    @BeforeAll
    public void setUp() {
        this.hotelUid = "049161bb-badd-4fa8-9d90-87c9a82b0668";
        this.paymentUid = "b83aee13-c823-48d4-b714-b5542291b5af";
        this.reservationUid = "e32d9ef3-1f81-44dd-be69-6f5b05ac6296";
        this.startDate = "2024-11-08";
        this.endDate = "2024-11-11";
        this.discount = 10;
        this.username = "Test Max";
        this.price = 27000;
        this.loyaltyStatus = "GOLD";
        this.request = new CreateReservationRequest(hotelUid, startDate, endDate);
        this.requestStartEndDate = new CreateReservationRequest(hotelUid, endDate, startDate);
        this.requestDatesInPast = new CreateReservationRequest(hotelUid, "2024-10-08", "2024-10-11");
        this.loyalty = new LoyaltyInfoResponseDTO(loyaltyStatus, discount,25);
        this.payment = new PaymentInfoDTO("PAID", price);
        this.response = new CreateReservationResponse(reservationUid, hotelUid, startDate, endDate, discount, "PAID", payment );
        this.loyaltyInfo = new LoyaltyInfoResponseDTO(loyaltyStatus, discount, 26);
        this.reservationDTO = new ReservationDTO("PAID", startDate, endDate, hotelUid, paymentUid);
        this.reservationDTOdelete = new ReservationDTO("CANCELED", startDate, endDate, hotelUid, paymentUid);
    }

    @Order(1)
    @Test
    public void saveReservationTest() throws URISyntaxException, ValidationErrorResponse, LoyaltyServiceException, ReservationServiceException, PaymentServiceException, LoyaltyException {
        when(reservationService.isHotelExist(hotelUid)).thenReturn(true);
        when(reservationService.getHotelPrice(hotelUid)).thenReturn(10000.0);
        when(loyaltyService.getLoyaltyForUser(username)).thenReturn(loyalty);
        when(loyaltyService.getDiscountForStatus(loyaltyStatus)).thenReturn(discount);
        when(paymentService.saveNewPayment(new PaymentInfoDTO("PAID", 27000)))
                .thenReturn(paymentUid);
        when(reservationService.saveReservation(reservationDTO,username)).thenReturn(response);

        CreateReservationResponse responseFromFun = this.reservationServiceLogic.makeReservation(username, request);

        System.out.println(responseFromFun.getStartDate());
        System.out.println(responseFromFun.getEndDate());
        System.out.println(responseFromFun.getReservationUid());
        System.out.println(responseFromFun.getDiscount());
        System.out.println(responseFromFun.getHotelUid());

        assertEquals(discount, responseFromFun.getDiscount());
        assertEquals(hotelUid, responseFromFun.getHotelUid());
        assertEquals(price, responseFromFun.getPayment().getPrice());
        assertEquals(startDate, responseFromFun.getStartDate());
        assertEquals(endDate, responseFromFun.getEndDate());
        verify(reservationService, times(1)).isHotelExist(hotelUid);
        verify(reservationService, times(1)).getHotelPrice(hotelUid);
        verify(loyaltyService, times(1)).getLoyaltyForUser(username);
        verify(paymentService, times(1)).saveNewPayment(payment);
        verify(reservationService, times(1)).saveReservation(reservationDTO, username);
    }

    @Order(2)
    @Test
    public void saveReservationHotelNotExistTest() throws URISyntaxException, ReservationServiceException {
        when(reservationService.isHotelExist(hotelUid)).thenReturn(false);
        ValidationErrorResponse thrown = assertThrows(ValidationErrorResponse.class, () -> {
            reservationServiceLogic.makeReservation(username, request);
        });
        assertEquals("Hotel is not exist", thrown.getMessage());
        verify(reservationService, times(1)).isHotelExist(hotelUid);
        verify(reservationService, times(0)).getHotelPrice(hotelUid);
    }

    @Order(3)
    @Test
    public void saveReservationStartDateIsBeforeEndDate() throws URISyntaxException, ReservationServiceException {
        ValidationErrorResponse thrown = assertThrows(ValidationErrorResponse.class, () -> {
            reservationServiceLogic.makeReservation(username, requestStartEndDate);
        });
        assertEquals("End date is before start date", thrown.getMessage());
        verify(reservationService, times(0)).getHotelPrice(hotelUid);
    }

    @Order(4)
    @Test
    public void saveReservationStartDatesInPast() throws URISyntaxException, ReservationServiceException {
        ValidationErrorResponse thrown = assertThrows(ValidationErrorResponse.class, () -> {
            reservationServiceLogic.makeReservation(username, requestDatesInPast);
        });
        assertEquals("Dates are in the past", thrown.getMessage());
        verify(reservationService, times(0)).getHotelPrice(hotelUid);
    }

    @Order(4)
    @Test
    public void deleteReservation() throws URISyntaxException, ErrorResponse, LoyaltyException, PaymentServiceException {
        when(reservationService.deleteReservationForUser(username, reservationUid)).thenReturn(reservationDTOdelete);
        Boolean result = reservationServiceLogic.deleteReservation(username, reservationUid);
        assertEquals(true, result);
    }

    @Order(5)
    @Test
    public void deleteReservationNotFound() throws URISyntaxException, ErrorResponse {
        when(reservationService.deleteReservationForUser(username, reservationUid)).thenReturn(null);
        ErrorResponse thrown = assertThrows(ErrorResponse.class, () -> {
            reservationServiceLogic.deleteReservation(username, reservationUid);
        });
        assertEquals("Reservation doesnt exist", thrown.getMessage());
    }
}
