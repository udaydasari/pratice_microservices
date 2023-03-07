package com.pratice_microservices.Orderservice.controller;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.github.tomakehurst.wiremock.core.WireMockConfiguration;
import com.github.tomakehurst.wiremock.junit5.WireMockExtension;
import com.pratice_microservices.Orderservice.OrderServiceConfig;
import com.pratice_microservices.Orderservice.entity.Order;
import com.pratice_microservices.Orderservice.model.OrderRequest;
import com.pratice_microservices.Orderservice.model.PaymentMode;
import com.pratice_microservices.Orderservice.repository.OrderRepository;
import com.pratice_microservices.Orderservice.service.OrderService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.RegisterExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.util.StreamUtils;

import java.io.IOException;
import java.nio.charset.Charset;
import java.util.Optional;

import static com.github.tomakehurst.wiremock.client.WireMock.*;


@SpringBootTest({"server.port=0"})
@EnableConfigurationProperties
@AutoConfigureMockMvc
@ContextConfiguration(classes = {OrderServiceConfig.class})
public class OrderControllerTest {

    @Autowired
    private OrderService orderService;
    @Autowired
    private OrderRepository orderRepository;
    @Autowired
    private MockMvc mockMvc;
    @RegisterExtension
    static WireMockExtension wireMockExtension
            =WireMockExtension.newInstance()
            .options(WireMockConfiguration
                    .wireMockConfig()
                    .port(8080))
            .build();

    private ObjectMapper objectMapper =
    new ObjectMapper()
            .findAndRegisterModules()
            .configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS,false)
            .configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES,false);
    @BeforeEach
    void setup() throws IOException {
        getProductDetailsResponse();
        doPayment();
        getPaymentDetails();
        reduceQuantity();

    }

    private void reduceQuantity() {
        wireMockExtension.stubFor(put(urlMatching("/product/reduceQuantity/*"))
                .willReturn(aResponse()
                        .withStatus(HttpStatus.OK.value())
                        .withHeader("Content-Type",MediaType.APPLICATION_JSON_VALUE)));
    }

    private void getPaymentDetails() throws IOException {
        wireMockExtension.stubFor(get(urlMatching("/payment/*"))
                .willReturn(aResponse().withStatus(HttpStatus.OK.value())
                        .withHeader("Content-Type",MediaType.APPLICATION_JSON_VALUE)
                        .withBody(StreamUtils.copyToString(
                                OrderController.class.getClassLoader()
                                        .getResourceAsStream("mock/GetPayment.json"), Charset.defaultCharset()
                        ))));

    }

    private void doPayment() {
        wireMockExtension.stubFor(post(urlEqualTo("/payment"))
                .willReturn(aResponse()
                         .withStatus(HttpStatus.OK.value())
                        .withHeader("Content-Type",MediaType.APPLICATION_JSON_VALUE)));

    }

    private void getProductDetailsResponse() throws IOException {
        //get /product/1
        wireMockExtension.stubFor(get("/product/1")
                .willReturn(aResponse().withStatus(HttpStatus.OK.value())
                        .withHeader("Content-Type", MediaType.APPLICATION_JSON_VALUE)
                        .withBody(StreamUtils.copyToString(
                                OrderController.class.getClassLoader()
                                        .getResourceAsStream("mock/GetProduct.json"), Charset.defaultCharset()
                        ))));

    }
    private OrderRequest getMockOrderRequest(){
        return OrderRequest.builder()
                .productId(1)
                .paymentMode(PaymentMode.CASH)
                .quantity(10)
                .totalAmount(200)
                .build();
    }


    @Test
    public void test_when_place_order_Dopayment_success() throws Exception {
        //first place order
        //get order by orderdi from DB and check
        // check output


        OrderRequest orderRequest= getMockOrderRequest();
        MvcResult mvcResult=mockMvc.perform(MockMvcRequestBuilders.post("/order/placeOrder")
                        //.with(jwt().authorities(new SimpleGrantedAuthority("Customer")))
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .content(objectMapper.writeValueAsString(orderRequest)
                        )).andExpect(MockMvcResultMatchers.status().isOk())
                .andReturn();

        String orderId = mvcResult.getResponse().getContentAsString();

        Optional<Order> order= orderRepository.findById(Long.valueOf(orderId));

        Assertions.assertTrue(order.isPresent());

        Order order1= order.get();
        Assertions.assertEquals(Long.parseLong(orderId),order1.getId());
        Assertions.assertEquals("PLACED",order1.getOrderStatus());
        Assertions.assertEquals(orderRequest.getTotalAmount(),order1.getAmount());
        Assertions.assertEquals(orderRequest.getQuantity(),order1.getQuantity());




    }



}