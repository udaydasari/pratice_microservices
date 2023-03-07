package com.pratice_microservices.Orderservice;

import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.cloud.loadbalancer.core.ServiceInstanceListSupplier;
import org.springframework.context.annotation.Bean;

@TestConfiguration
public class OrderServiceConfig {
    @Bean
    public ServiceInstanceListSupplier  serviceInstanceListSupplier(){
        return new TestServiceInstanceListSuppplier();
    }
}
