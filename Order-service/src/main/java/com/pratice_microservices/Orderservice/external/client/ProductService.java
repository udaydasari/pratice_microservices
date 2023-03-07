package com.pratice_microservices.Orderservice.external.client;

import com.pratice_microservices.Orderservice.exception.CustomException;
import com.pratice_microservices.Orderservice.external.request.PaymentRequest;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@FeignClient(name = "PRODUCT-SERVICE/product")
public interface ProductService {
    @PutMapping("/reduceQuantity/{id}")
    ResponseEntity<Void> reduceQuantity(@PathVariable("id") long productId, @RequestParam long quantity);

    default void fallback(Exception e){
        throw new CustomException("Payment service is not available ","UNAVAILABLE",500);

    }
    }
