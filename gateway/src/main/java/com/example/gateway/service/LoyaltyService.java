package com.example.gateway.service;

import com.example.gateway.dto.LoyaltyInfoResponseDTO;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.client.RestTemplate;

import java.net.URI;
import java.net.URISyntaxException;

@Service
public class LoyaltyService {

    @Value("${loyalty.service.url}")
    private String basicLoyalty;

    private final int BRONZE_DISCOUNT = 5;

    private final int SILVER_DISCOUNT = 7;

    private final int GOLD_DISCOUNT = 10;

    public LoyaltyInfoResponseDTO getLoyaltyForUser(String username) throws URISyntaxException {
        URI uri = new URI(this.basicLoyalty.toString() + "/loyalty");
        System.out.println(uri.toString());
        RestTemplate restTemplate = new RestTemplate();
        HttpHeaders headers = new HttpHeaders();
        headers.add("X-User-Name",username);
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity entity = new HttpEntity(headers);
        ResponseEntity<LoyaltyInfoResponseDTO> result = restTemplate.exchange(uri, HttpMethod.GET, entity, LoyaltyInfoResponseDTO.class);
        System.out.println(result.getBody());
        System.out.println(result);
        return result.getBody();
    }

    public int getDiscountForStatus(String status) throws URISyntaxException {
        switch (status){
            case "SILVER": return this.SILVER_DISCOUNT;
            case "GOLD": return this.GOLD_DISCOUNT;
            default:return this.BRONZE_DISCOUNT;
        }
    }

    public LoyaltyInfoResponseDTO addNewBooking(String username) throws URISyntaxException {
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
        return result.getBody();
    }

    public LoyaltyInfoResponseDTO subtractBooking(String username) throws URISyntaxException {
        URI uri = new URI(this.basicLoyalty.toString() + "/loyalty");
        System.out.println(uri.toString());
        RestTemplate restTemplate = new RestTemplate();
        HttpHeaders headers = new HttpHeaders();
        headers.add("X-User-Name", username);
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity entity = new HttpEntity(headers);
        ResponseEntity<LoyaltyInfoResponseDTO> result = restTemplate.exchange(uri, HttpMethod.DELETE, entity, LoyaltyInfoResponseDTO.class);
        System.out.println(result.getBody());
        System.out.println(result);
        return result.getBody();
    }


}
