package com.praticemicroservice.Paymentservice.controller;

import com.praticemicroservice.Paymentservice.model.PaymentRequest;
import com.praticemicroservice.Paymentservice.model.PaymentResponse;
import com.praticemicroservice.Paymentservice.service.PaymentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/payment")
public class PaymentController {
    @Autowired
    private PaymentService paymentService;

    @PostMapping
    public ResponseEntity<Long> doPayment(@RequestBody  PaymentRequest paymentRequest){
        return new ResponseEntity<>(
                paymentService.doPayment(paymentRequest), HttpStatus.OK
        );}
    @GetMapping("/{orderId}")
        public ResponseEntity<PaymentResponse> getPaymentDetailsByOrderId(@PathVariable long orderId){
        return new ResponseEntity<>(
                paymentService.getPaymentDetailsByOrderId(orderId),HttpStatus.OK
        );

        }







}

