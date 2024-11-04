package com.example.gateway.service;

import com.example.gateway.dto.PaymentInfoDTO;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import java.net.URI;
import java.net.URISyntaxException;

@Service
public class PaymentService {
    @Value("${payment.service.url}")
    private String basicPayment;

    public PaymentInfoDTO getPaymentInfo(String paymentUid) throws URISyntaxException {
        URI uri = new URI(this.basicPayment.toString() + "/payment/"+paymentUid);
        System.out.println(uri.toString());
        RestTemplate restTemplate = new RestTemplate();
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity entity = new HttpEntity(headers);
        ResponseEntity<PaymentInfoDTO> result = restTemplate.exchange(
                uri,
                HttpMethod.GET,
                entity,
                PaymentInfoDTO.class);
        System.out.println(result.getBody());
        return result.getBody();
    }

    public String saveNewPayment(PaymentInfoDTO paymentInfoDTO) throws URISyntaxException {
        URI uri = new URI(this.basicPayment.toString() + "/payment");
        System.out.println(uri.toString());
        RestTemplate restTemplate = new RestTemplate();
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity entity = new HttpEntity(paymentInfoDTO, headers);
        ResponseEntity<String> result = restTemplate.exchange(
                uri,
                HttpMethod.POST,
                entity,
                String.class);
        System.out.println(result.getBody());
        return result.getBody();
    }

    public void deletePayment(String paymentUid) throws URISyntaxException {
        URI uri = new URI(this.basicPayment.toString() + "/payment/" + paymentUid);
        System.out.println(uri.toString());
        RestTemplate restTemplate = new RestTemplate();
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity entity = new HttpEntity(headers);
        ResponseEntity<String> result = restTemplate.exchange(
                uri,
                HttpMethod.DELETE,
                entity,
                String.class);
        System.out.println(result.getBody());
    }
}
