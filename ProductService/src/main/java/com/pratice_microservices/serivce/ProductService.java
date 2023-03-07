package com.pratice_microservices.serivce;

import com.pratice_microservices.model.ProductRequest;
import com.pratice_microservices.model.ProductResponse;

public interface ProductService {
    long addProduct(ProductRequest productRequest);

    ProductResponse getProductById(long productId);

    void reduceQuantity(long productId, long quantity);
}
