package com.pratice_microservices.Orderservice.repository;

import com.pratice_microservices.Orderservice.entity.Order;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface OrderRepository extends JpaRepository<Order,Long> {

}
