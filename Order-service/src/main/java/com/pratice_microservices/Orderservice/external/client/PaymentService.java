package com.pratice_microservices.Orderservice.external.client;

import com.pratice_microservices.Orderservice.exception.CustomException;
import com.pratice_microservices.Orderservice.external.request.PaymentRequest;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@CircuitBreaker(name = "external",fallbackMethod = "fallback")
@FeignClient(name = "PAYMENT-SERVICE/payment")
public interface PaymentService {
    @PostMapping
    ResponseEntity<Long> doPayment(@RequestBody PaymentRequest paymentRequest);

    default void fallback(Exception e){
        throw new CustomException("Payment service is not available ","UNAVAILABLE",500);

    }

}