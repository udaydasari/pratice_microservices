package com.pratice_microservices.Orderservice.config;

import com.pratice_microservices.Orderservice.external.decoder.CustomerErrorDecoder;
import feign.codec.ErrorDecoder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class FeignConfig {
    @Bean
    ErrorDecoder errorDecoder(){
        return new CustomerErrorDecoder();

    }

}
