package com.praticemicroservice.Paymentservice.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.NamedEntityGraph;
import java.time.Instant;
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class PaymentResponse {
    private  long paymentId;
    private String status;
    private PaymentMode paymentMode;
    private long amount;
    private Instant paymentDate;
    private long orderId;

}
