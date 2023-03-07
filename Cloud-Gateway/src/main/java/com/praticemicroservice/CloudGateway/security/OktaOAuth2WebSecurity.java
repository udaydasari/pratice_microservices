//package com.praticemicroservice.CloudGateway.security;//package com.praticemicroservice.CloudGateway.security;
////
////import org.springframework.context.annotation.Bean;
////import org.springframework.context.annotation.Configuration;
////import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity;
////import org.springframework.security.config.web.server.ServerHttpSecurity;
////import org.springframework.security.web.SecurityFilterChain;
////import org.springframework.security.web.server.SecurityWebFilterChain;
////
////@Configuration
////@EnableWebFluxSecurity
////public class OktaOAuth2WebSecurity {
////    @Bean
////    public SecurityWebFilterChain securityFilterChain(ServerHttpSecurity httpSecurity)
////    {
////        httpSecurity
////                .authorizeExchange()
////                .anyExchange()
////                .authenticated()
////                .and()
////                .oauth2Login()
////                .and()
////                .oauth2ResourceServer()
////                .jwt();
////        return httpSecurity.build();
////    }
////
////}
//
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//import org.springframework.security.config.annotation.web.builders.HttpSecurity;
//import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
//import org.springframework.security.config.web.server.ServerHttpSecurity;
//import org.springframework.security.web.server.SecurityWebFilterChain;
//
//@Configuration
//public class OktaOAuth2WebSecurity  {
//
//
//    @Bean
//    public SecurityWebFilterChain configure(ServerHttpSecurity httpSecurity) throws Exception {
//         return httpSecurity.authorizeExchange().pathMatchers("/").permitAll().and().build();
////                .authorizeRequests().antMatchers("/").permitAll();
//
//
//    }
//
//}
