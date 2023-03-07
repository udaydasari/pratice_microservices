package com.pratice_microservices.Orderservice.service;

import brave.messaging.ProducerResponse;
import com.pratice_microservices.Orderservice.entity.Order;
import com.pratice_microservices.Orderservice.exception.CustomException;
import com.pratice_microservices.Orderservice.external.client.PaymentService;
import com.pratice_microservices.Orderservice.external.client.ProductService;
import com.pratice_microservices.Orderservice.external.request.PaymentRequest;
import com.pratice_microservices.Orderservice.model.OrderRequest;
import com.pratice_microservices.Orderservice.model.OrderResponse;
import com.pratice_microservices.Orderservice.model.PaymentResponse;
import com.pratice_microservices.Orderservice.model.ProductResponse;
import com.pratice_microservices.Orderservice.repository.OrderRepository;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;


import java.time.Instant;
import java.util.function.ObjDoubleConsumer;

@Service
@Log4j2
public class  OrderServiceImpl implements  OrderService{
    @Autowired
    private OrderRepository orderRepository;
    @Autowired
    private ProductService productService;
    @Autowired
    private PaymentService paymentService;

    @Autowired
    private RestTemplate restTemplate;
    @Override
    public long placeOrder(OrderRequest orderRequest) {
        //OrderEntity ->save the data with status order created
        //product service ->Block products(Reduce the quantity
        //Payment Serive->payments->success->completed.else no
        //cancelled
        log.info("placing order request :{}",orderRequest);
        productService.reduceQuantity(orderRequest.getProductId(),orderRequest.getQuantity());

        log.info("created order with status created ");
        Order order =Order.builder()
                .amount(orderRequest.getTotalAmount())
                .orderStatus("CREATED")
                .productId((orderRequest.getProductId()))
                .orderDate(Instant.now())
                .quantity(orderRequest.getQuantity())
                .build();
        order=orderRepository.save(order);

        log.info("calling payment service to complete the payment ");
        PaymentRequest paymentRequest= PaymentRequest.builder()
                .orderId(order.getId())
                        .paymentMode(orderRequest.getPaymentMode())
                                .amount(orderRequest.getTotalAmount())
                                        .build();
        String orderStatus= null;
        try {
            paymentService.doPayment(paymentRequest);
            log.info("Payment done sucessfully");
            orderStatus="PLACED";

        }
        catch (Exception e)
        {
            log.error("Error occured in payment ");
            orderStatus="PAYMENT_FAILED";
        }
        order.setOrderStatus(orderStatus);
        orderRepository.save(order);

        log.info("Order placed successfully with order Id :{}",order.getId());


        return order.getId();
    }

    @Override
    public OrderResponse getOrderDetails(long orderId) {
        log.info("Get order information for the order id:{}",orderId);
        Order order= orderRepository.findById(orderId)
                .orElseThrow(()-> new CustomException("Order not found ","ORDER_NOT_FOUND",404));
        log.info("Invoking product for product id:{}",order.getProductId());



        ProductResponse productResponse =
                restTemplate.getForObject(
                        "http://PRODUCT-SERVICE/product/"+order.getProductId()
                        ,ProductResponse.class
                );

        OrderResponse.ProductDetails productDetails=OrderResponse.ProductDetails
                .builder()
                .ProductName(productResponse.getProductName())
                .productId(productResponse.getProductId())
                .quantity(productResponse.getQuantity())
                .price(productResponse.getPrice())
                .build();


        PaymentResponse paymentResponse=
                restTemplate.getForObject(
                        "http://PAYMENT-SERVICE/payment/"+order.getId(), PaymentResponse.class
                );

        OrderResponse.PaymentDetails paymentDetails= OrderResponse.PaymentDetails.builder()
                .paymentId(paymentResponse.getPaymentId())
                .paymentDate(paymentResponse.getPaymentDate())
                .status(paymentResponse.getStatus())
                .paymentMode(paymentResponse.getPaymentMode())
                .build();


        OrderResponse orderResponse= OrderResponse.builder()
                .orderId(order.getId())
                .orderStatus(order.getOrderStatus())
                .orderDate(order.getOrderDate())
                .amount(order.getAmount())
                .productDetails(productDetails)
                .paymentDetails(paymentDetails)
                .build();
        return orderResponse;
    }

}
