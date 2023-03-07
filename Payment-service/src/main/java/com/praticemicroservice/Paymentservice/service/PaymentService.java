package com.praticemicroservice.Paymentservice.service;

import com.praticemicroservice.Paymentservice.model.PaymentRequest;
import com.praticemicroservice.Paymentservice.model.PaymentResponse;

public interface PaymentService {
    long doPayment(PaymentRequest paymentRequest);


    PaymentResponse getPaymentDetailsByOrderId(long orderId);
}
