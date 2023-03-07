package com.pratice_microservices.repository;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.pratice_microservices.entity.Product;

@Repository
public interface ProductRepository extends JpaRepository<Product,Long> {
}
