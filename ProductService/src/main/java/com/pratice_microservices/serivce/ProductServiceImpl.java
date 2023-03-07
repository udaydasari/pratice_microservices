package com.pratice_microservices.serivce;

import com.pratice_microservices.exception.ProductServiceCustomException;
import com.pratice_microservices.model.ProductRequest;
import com.pratice_microservices.model.ProductResponse;
import com.pratice_microservices.repository.ProductRepository;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.pratice_microservices.entity.Product;

import static org.springframework.beans.BeanUtils.copyProperties;

@Service
@Log4j2
public class ProductServiceImpl implements ProductService{
    @Autowired
    private ProductRepository productRepository;

    @Override
    public long addProduct(ProductRequest productRequest) {
        log.info("Adding product..");
        Product product =Product.builder().productName(productRequest.getName())
                .quantity(productRequest.getQuantity())
                .price(productRequest.getPrice())
                .build();
        productRepository.save(product);
        log.info("producted created ");
         return product.getProductId();

    }

    @Override
    public ProductResponse getProductById(long productId) {
        log.info("Get the product for productBy id: {}",productId);
        Product product =
                productRepository.findById(productId)
                        .orElseThrow(()-> new ProductServiceCustomException("Product not found","PRODUCT_NOT_FOUND"));
        ProductResponse productResponse= new ProductResponse();
        copyProperties(product,productResponse);
        return productResponse;
    }

    @Override
    public void reduceQuantity(long productId, long quantity) {
        log.info("reduce quantity {} for Id {}",quantity,productId);
        Product product= productRepository.findById(productId)
                .orElseThrow(()->new ProductServiceCustomException("No such product exist","PRODUCT_NOT_FOUND"));
        if(product.getQuantity()<quantity){
            throw new ProductServiceCustomException("Product does have sufficent quantity ","INSUFFICENT_QUANTUITY");

        }
        product.setQuantity(product.getQuantity()-quantity);
        productRepository.save(product);
        log.info("Product quantity updated successfully  ");

    }
}
