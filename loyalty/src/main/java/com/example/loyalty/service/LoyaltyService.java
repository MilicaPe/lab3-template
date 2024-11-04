package com.example.loyalty.service;

import com.example.loyalty.model.Loyalty;
import com.example.loyalty.model.Status;
import com.example.loyalty.repository.LoyaltyRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class LoyaltyService {
    @Autowired
    LoyaltyRepository loyaltyRepository;

    public Loyalty addNewBooking(String username){
        Loyalty loyalty = loyaltyRepository.getLoyaltyByUsername(username);
        int booking = loyalty.getReservationCount();
        loyalty.setReservationCount(booking + 1);
        checkLoyaltyStatus(loyalty);
        Loyalty updated = loyaltyRepository.save(loyalty);
        return updated;
    }

    private void checkLoyaltyStatus(Loyalty loyalty){
        int booking = loyalty.getReservationCount();
        if (booking > 20){
            loyalty.setStatus("GOLD");
        } else if (booking > 10) {
            loyalty.setStatus("SILVER");
        }
        else {
            loyalty.setStatus("BRONZE");
        }
    }

    public Loyalty subtractBooking(String username) {
        Loyalty loyalty = loyaltyRepository.getLoyaltyByUsername(username);
        int booking = loyalty.getReservationCount();
        loyalty.setReservationCount(booking - 1);
        checkLoyaltyStatus(loyalty);
        Loyalty updated = loyaltyRepository.save(loyalty);
        return updated;
    }
}
