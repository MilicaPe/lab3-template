package com.example.gateway.service;

import com.example.gateway.circuit.breaker.CircuitBreaker;
import com.example.gateway.dto.LoyaltyException;
import com.example.gateway.dto.LoyaltyInfoResponseDTO;
import com.example.gateway.dto.error.LoyaltyServiceException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.net.URI;
import java.net.URISyntaxException;
import java.time.Duration;

@Service
public class LoyaltyService {

    @Value("${loyalty.service.url}")
    private String basicLoyalty;

    private final int BRONZE_DISCOUNT = 5;

    private final int SILVER_DISCOUNT = 7;

    private final int GOLD_DISCOUNT = 10;

    @Autowired
    CircuitBreaker loyaltyCircuitBreaker;


    public LoyaltyInfoResponseDTO getLoyaltyForUser(String username) throws LoyaltyServiceException {
        if(!this.loyaltyCircuitBreaker.allowRequest()) {
            throw new LoyaltyServiceException();
        }
        try {
            URI uri = new URI(this.basicLoyalty.toString() + "/loyalty");
            System.out.println(uri.toString());
            RestTemplate restTemplate = new RestTemplate();
            HttpHeaders headers = new HttpHeaders();
            headers.add("X-User-Name", username);
            headers.setContentType(MediaType.APPLICATION_JSON);
            HttpEntity entity = new HttpEntity(headers);
            ResponseEntity<LoyaltyInfoResponseDTO> result = restTemplate.exchange(uri, HttpMethod.GET, entity, LoyaltyInfoResponseDTO.class);
            System.out.println(result.getBody());
            System.out.println(result);
            if (result.getStatusCode().value() == 500) {
                this.loyaltyCircuitBreaker.recordFailure();
                throw new LoyaltyServiceException();
            }
            this.loyaltyCircuitBreaker.recordSuccess();
            return result.getBody();
        } catch (Exception e){
            this.loyaltyCircuitBreaker.recordFailure();
            throw new LoyaltyServiceException();
        }
    }

    public int getDiscountForStatus(String status) throws URISyntaxException {
        switch (status){
            case "SILVER": return this.SILVER_DISCOUNT;
            case "GOLD": return this.GOLD_DISCOUNT;
            default:return this.BRONZE_DISCOUNT;
        }
    }

    public LoyaltyInfoResponseDTO addNewBooking(String username) throws URISyntaxException, LoyaltyServiceException, LoyaltyException {
        if(!this.loyaltyCircuitBreaker.allowRequest()) {
            throw new LoyaltyException();
        }
        try {
        URI uri = new URI(this.basicLoyalty.toString() + "/loyalty");
        System.out.println(uri.toString());
        RestTemplate restTemplate = new RestTemplate();
        HttpHeaders headers = new HttpHeaders();
        headers.add("X-User-Name", username);
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity entity = new HttpEntity(headers);
        ResponseEntity<LoyaltyInfoResponseDTO> result = restTemplate.exchange(uri, HttpMethod.POST, entity, LoyaltyInfoResponseDTO.class);
        System.out.println(result.getBody());
        System.out.println(result);
        this.loyaltyCircuitBreaker.recordSuccess();
        return result.getBody();
        } catch (Exception e){
            this.loyaltyCircuitBreaker.recordFailure();
            throw new LoyaltyException();
        }
    }

    public LoyaltyInfoResponseDTO subtractBooking(String username) throws URISyntaxException, LoyaltyServiceException, LoyaltyException {
        if(!this.loyaltyCircuitBreaker.allowRequest()) {
            System.out.println("Ne da CB");
            throw new LoyaltyException();
        }
        try {
            URI uri = new URI(this.basicLoyalty.toString() + "/loyalty");
            System.out.println(uri.toString());
            RestTemplate restTemplate = new RestTemplate();
            HttpHeaders headers = new HttpHeaders();
            headers.add("X-User-Name", username);
            headers.setContentType(MediaType.APPLICATION_JSON);
            HttpEntity entity = new HttpEntity(headers);
            System.out.println(" +++++++++++++  MILICE  !!!! ++++++++++++++++++ ");
            System.out.println(restTemplate.toString());
            ResponseEntity<LoyaltyInfoResponseDTO> result = restTemplate.exchange(uri, HttpMethod.DELETE, entity, LoyaltyInfoResponseDTO.class);
            System.out.println(result.getBody());
            System.out.println(result);
            this.loyaltyCircuitBreaker.recordSuccess();
            return result.getBody();
        } catch (Exception e){
            this.loyaltyCircuitBreaker.recordFailure();
            throw new LoyaltyException();
        }
    }


}
