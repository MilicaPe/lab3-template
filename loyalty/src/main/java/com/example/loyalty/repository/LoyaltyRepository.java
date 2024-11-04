package com.example.loyalty.repository;

import com.example.loyalty.model.Loyalty;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface LoyaltyRepository extends JpaRepository<Loyalty, Long> {

    Loyalty getLoyaltyByUsername(String username);


}
