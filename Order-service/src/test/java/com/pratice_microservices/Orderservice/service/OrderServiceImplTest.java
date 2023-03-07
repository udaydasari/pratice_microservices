package com.pratice_microservices.Orderservice.service;

import com.pratice_microservices.Orderservice.entity.Order;
import com.pratice_microservices.Orderservice.exception.CustomException;
import com.pratice_microservices.Orderservice.external.client.PaymentService;
import com.pratice_microservices.Orderservice.external.client.ProductService;
import com.pratice_microservices.Orderservice.external.request.PaymentRequest;
import com.pratice_microservices.Orderservice.model.*;
import com.pratice_microservices.Orderservice.repository.OrderRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import java.time.Instant;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;

@SpringBootTest
public class OrderServiceImplTest {
    @Mock
    private OrderRepository orderRepository;
    @Mock
    private ProductService productService;
    @Mock
    private PaymentService paymentService;
    @Mock
    private RestTemplate restTemplate;

    @InjectMocks
    OrderService orderService = new OrderServiceImpl();
    @DisplayName("Get order success scenario")
    @Test
    void test_When_Order_Success(){
        Order order=getMockOrder();
        //Mocking
        when(orderRepository.findById(anyLong()))
                .thenReturn(Optional.of(order));

        when(restTemplate.getForObject("http://PRODUCT-SERVICE/product/"+order.getProductId()
                , ProductResponse.class)).thenReturn(getMockProductResponse());

        when(restTemplate.getForObject("http://PAYMENT-SERVICE/payment/"+order.getId(), PaymentResponse.class
        )).thenReturn(getMockPaymentResponse());
        //Actual method
        OrderResponse orderResponse=orderService.getOrderDetails(1);
        //verification
        verify(orderRepository,times(1)).findById(anyLong());
        verify(restTemplate,times(1)).getForObject("http://PRODUCT-SERVICE/product/"+order.getProductId()
                , ProductResponse.class);
        verify(restTemplate,times(1)).getForObject("http://PAYMENT-SERVICE/payment/"+order.getId(), PaymentResponse.class
        );
        //Assert
        assertNotNull(orderResponse);
        assertEquals(order.getId(),orderResponse.getOrderId());



    }





    @DisplayName("Get order failure scenario")
    @Test
    void test_when_order_not_found(){

        when(orderRepository.findById(anyLong()))
                .thenReturn(Optional.ofNullable(null));

        //OrderResponse orderResponse=orderService.getOrderDetails(1);

        CustomException customException=assertThrows(CustomException.class,
                ()->orderService.getOrderDetails(1));

        assertEquals("ORDER_NOT_FOUND",customException.getErrorCode());

        assertEquals(404,customException.getStatus());

        verify(orderRepository,times(1))
                .findById(anyLong());


    }

    @DisplayName("place order - success scenario")
    @Test
    void test_when_place_order_Sucsess(){
        Order order= getMockOrder();
        OrderRequest orderRequest = getMockOrderRequest();
        when(orderRepository.save(any(Order.class))).thenReturn(order);


        when(productService.reduceQuantity(anyLong(),anyLong()))
                .thenReturn(new ResponseEntity<Void>(HttpStatus.OK));

        when(paymentService.doPayment(any(PaymentRequest.class)))
                .thenReturn(new ResponseEntity<Long>(1L,HttpStatus.OK));
        long orderId = orderService.placeOrder(orderRequest);

        verify(orderRepository,times(2)).save(any());

        verify(productService,times(1)).reduceQuantity(anyLong(),anyLong());

        verify(paymentService,times(1)).doPayment(any(PaymentRequest.class));

        assertEquals(order.getId(),orderId);




    }
@Test
    void test_when_place_order_payment_fails_then_order_placed(){

        Order order= getMockOrder();
        OrderRequest orderRequest = getMockOrderRequest();
        when(orderRepository.save(any(Order.class))).thenReturn(order);


        when(productService.reduceQuantity(anyLong(),anyLong()))
                .thenReturn(new ResponseEntity<Void>(HttpStatus.OK));

        when(paymentService.doPayment(any(PaymentRequest.class)))
                .thenThrow(new RuntimeException());
        long orderId = orderService.placeOrder(orderRequest);

        verify(orderRepository,times(2)).save(any());

        verify(productService,times(1)).reduceQuantity(anyLong(),anyLong());

        verify(paymentService,times(1)).doPayment(any(PaymentRequest.class));

        assertEquals(order.getId(),orderId);


    }




    private OrderRequest getMockOrderRequest() {

        return OrderRequest.builder()
                .productId(1)
                .quantity(10)
                .paymentMode(PaymentMode.CASH)
                .totalAmount(100)
                .build();
    }


    private PaymentResponse getMockPaymentResponse() {
        return PaymentResponse.builder()
                .paymentId(1)
                .paymentDate(Instant.now())
                .paymentMode(PaymentMode.CASH)
                .amount(200)
                .orderId(1)
                .status("ACCEPTED")
                .build();

    }

    private ProductResponse getMockProductResponse() {
return ProductResponse.builder()
        .productId(2)
        .ProductName("iphone")
        .price(100)
        .quantity(200)
        .build();

    }

    private Order getMockOrder() {
        return Order.builder()
                .orderStatus("PLACED")
                .orderDate(Instant.now())
                .id(1)
                .amount(100)
                .quantity(200)
                .productId(2)
                .build();
    }


}