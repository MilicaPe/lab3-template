package com.example.gateway.service;

import com.example.gateway.circuit.breaker.CircuitBreaker;
import com.example.gateway.dto.PaymentInfoDTO;
import com.example.gateway.dto.error.PaymentServiceException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import java.net.URI;
import java.net.URISyntaxException;
import java.time.Duration;

@Service
public class PaymentService {
    @Value("${payment.service.url}")
    private String basicPayment;

    @Autowired
    CircuitBreaker paymentCircuitBreaker;

    public PaymentInfoDTO getPaymentInfo(String paymentUid) throws URISyntaxException, PaymentServiceException {
        if(! paymentCircuitBreaker.allowRequest()){
            this.paymentCircuitBreaker.recordFailure();
            throw new PaymentServiceException();
        }
        try {
            URI uri = new URI(this.basicPayment.toString() + "/payment/" + paymentUid);
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
            if (result.getStatusCode().value() == 500) {
                this.paymentCircuitBreaker.recordFailure();
                throw new PaymentServiceException();
            }
            this.paymentCircuitBreaker.recordSuccess();
            return result.getBody();
        } catch (Exception e){
            this.paymentCircuitBreaker.recordFailure();
            throw new PaymentServiceException();
        }
    }

    public String saveNewPayment(PaymentInfoDTO paymentInfoDTO) throws URISyntaxException, PaymentServiceException {
        if(! paymentCircuitBreaker.allowRequest()){
            this.paymentCircuitBreaker.recordFailure();
            throw new PaymentServiceException();
        }
        try {
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
            this.paymentCircuitBreaker.recordSuccess();
            return result.getBody();
        }catch (Exception e){
            this.paymentCircuitBreaker.recordFailure();
            throw new PaymentServiceException();
        }
    }

    public void deletePayment(String paymentUid) throws URISyntaxException, PaymentServiceException {
        if(! paymentCircuitBreaker.allowRequest()){
            this.paymentCircuitBreaker.recordFailure();
            throw new PaymentServiceException();
        }
        try {
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
            this.paymentCircuitBreaker.recordSuccess();
        }catch (Exception e){
            this.paymentCircuitBreaker.recordFailure();
            throw new PaymentServiceException();
        }
    }
}
