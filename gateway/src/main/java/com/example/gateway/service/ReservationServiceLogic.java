package com.example.gateway.service;

import ch.qos.logback.core.model.IncludeModel;
import com.example.gateway.dto.*;
import com.example.gateway.dto.error.*;
import com.example.gateway.redis.RedisService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import redis.clients.jedis.Jedis;

import java.net.URI;
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
    private LoyaltyService loyaltyService;

    @Autowired
    private ReservationService reservationService;

    @Autowired
    private RedisService redisService;

    private final String paid = "PAID";
    private final String canceled = "CANCELED";




    public CreateReservationResponse makeReservation(String username, CreateReservationRequest request) throws URISyntaxException, ValidationErrorResponse, ReservationServiceException, LoyaltyException, LoyaltyServiceException, PaymentServiceException {
        String newReservationUid = null;
        String paymentUid = null;
        try {
            validateReservationRequest(request);
            int numOfNights = countNumberOfNights(request);
            Double pricePerNight = reservationService.getHotelPrice(request.getHotelUid());
            double fullPrice = numOfNights * pricePerNight;

            LoyaltyInfoResponseDTO loyalty = loyaltyService.getLoyaltyForUser(username);
            int discount = loyaltyService.getDiscountForStatus(loyalty.getStatus());
            int endPrice = (int) (fullPrice - (fullPrice * discount / 100));

            PaymentInfoDTO newPaymentInfo = new PaymentInfoDTO(paid, endPrice);
            paymentUid = this.paymentService.saveNewPayment(newPaymentInfo);

            ReservationDTO reservationDTO = new ReservationDTO();
            reservationDTO.setPaymentUid(paymentUid);
            reservationDTO.setHotelUid(request.getHotelUid());
            reservationDTO.setStartDate(request.getStartDate());
            reservationDTO.setEndDate(request.getEndDate());
            reservationDTO.setStatus(paid);

            CreateReservationResponse response = this.reservationService.saveReservation(reservationDTO, username);
            newReservationUid = response.getReservationUid();
            response.setPayment(newPaymentInfo);
            response.setDiscount(discount);
            LoyaltyInfoResponseDTO loyaltyInfo = loyaltyService.addNewBooking(username);

            return response;
        }catch (LoyaltyServiceException e) {
            throw new LoyaltyServiceException();
        }catch (PaymentServiceException e){
            throw new PaymentServiceException();
        } catch (LoyaltyException e) {   //// откатить все операции
            this.reservationService.deleteReservationForUser(username, newReservationUid);
            this.paymentService.deletePayment(paymentUid);
            throw  new LoyaltyException();
        }
    }
    private int countNumberOfNights(CreateReservationRequest request){
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        LocalDate startDate = LocalDate.parse(request.getStartDate(), formatter);
        LocalDate endDate = LocalDate.parse(request.getEndDate(), formatter);
        int numOfNights = (int) ChronoUnit.DAYS.between(startDate, endDate);
        return numOfNights;
    }

    private boolean validateReservationRequest(CreateReservationRequest request) throws URISyntaxException, ValidationErrorResponse, ReservationServiceException {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        LocalDate startDate = LocalDate.parse(request.getStartDate(), formatter);
        LocalDate endDate = LocalDate.parse(request.getEndDate(), formatter);
        LocalDate now = LocalDate.now();
        if ((startDate.isBefore(now)) || (endDate.isBefore(now)))
            throw new ValidationErrorResponse("Dates are in the past", new ArrayList<>());
        if (endDate.isBefore(startDate)){
            throw new ValidationErrorResponse("End date is before start date", new ArrayList<>());
        }
        try {
            if (!reservationService.isHotelExist(request.getHotelUid()))
                throw new ValidationErrorResponse("Hotel is not exist", new ArrayList<>());
        }catch (Exception e){
            throw new ReservationServiceException();
        }

        return true;
    }

    public boolean deleteReservation(String username, String reservationUid) throws URISyntaxException, ErrorResponse, PaymentServiceException, LoyaltyException {
        ReservationDTO r = reservationService.deleteReservationForUser(username, reservationUid);
        if (r == null || r.getStatus().equals("PAID")) {
            System.out.println("reservation " + r);
            throw new ErrorResponse("Reservation doesnt exist");
        }
        try {
            this.paymentService.deletePayment(r.getPaymentUid());
            this.loyaltyService.subtractBooking(username);
            return true;
        } catch (PaymentServiceException e) {
            throw new PaymentServiceException();
        } catch (LoyaltyServiceException | LoyaltyException e) {
            redisService.set("loyality", username);
            throw new LoyaltyException();
        }
    }


//    @Scheduled(initialDelayString = "${greeting.initialdelay}", fixedRateString = "${greeting.fixedrate}")
//    public void tryToSendRequestsFromQueue() throws InterruptedException, URISyntaxException {
//        System.out.println("Praznjenje reda");
//        while (true) {
//            String username = null;
//            username = redisService.get("loyalty"); // Извлекаем из начала очереди
//            if (username == null) {
//                Thread.sleep(1000); // Пауза на 1 секунду, если очередь пуста
//                continue;
//            }
//            try {
//                loyaltyService.subtractBooking(username);// probaj  poyovi metodu sa tim username i to je to
//                // uspesno ---- ukloni iz reda
//            }catch (LoyaltyServiceException  | LoyaltyException e){
//                redisService.set("loyalty", username);
//            }
//
//        }
//    }


}
