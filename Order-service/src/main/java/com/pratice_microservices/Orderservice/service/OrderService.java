package com.pratice_microservices.Orderservice.service;

import com.pratice_microservices.Orderservice.model.OrderRequest;
import com.pratice_microservices.Orderservice.model.OrderResponse;

public interface OrderService {
    long placeOrder(OrderRequest orderRequest);

    OrderResponse getOrderDetails(long orderId);
}
