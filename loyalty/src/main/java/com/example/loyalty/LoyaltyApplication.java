package com.example.loyalty;

import com.example.loyalty.model.Loyalty;
import com.example.loyalty.model.Status;
import com.example.loyalty.repository.LoyaltyRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class LoyaltyApplication {
	@Autowired
	static
	LoyaltyRepository loyaltyRepository;

	public static void main(String[] args) {
		SpringApplication.run(LoyaltyApplication.class, args);
//		Loyalty l = new Loyalty(1L, "Test Max", 25, Status.GOLD,  10);
//		loyaltyRepository.save(l);
	}

}
