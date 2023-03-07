package com.pratice_microservices.controller;

import com.pratice_microservices.model.ProductRequest;
import com.pratice_microservices.model.ProductResponse;
import com.pratice_microservices.serivce.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/product")
public class ProductController {
    @Autowired
    private ProductService productService;
//    @PreAuthorize("hasAuthority('Admin')")
    @PostMapping
    public ResponseEntity<Long> addProduct(@RequestBody ProductRequest productRequest){
        long productId =productService.addProduct(productRequest);
        return new ResponseEntity<>(productId, HttpStatus.CREATED);


    }
//    @PreAuthorize("hasAuthority('Admin') || hasAuthority('Customer') || hasAuthority('SCOPE_internal')")
    @GetMapping("/{id}")
    public ResponseEntity<ProductResponse> getProductById(@PathVariable("id") long productId){
    ProductResponse productResponse= productService.getProductById(productId);

    return new ResponseEntity<>(productResponse,HttpStatus.CREATED);

    }

    @PutMapping("/reduceQuantity/{id}")
    public ResponseEntity<Void> reduceQuantity(@PathVariable("id") long productId,@RequestParam long quantity){
        productService.reduceQuantity(productId,quantity);
        return new ResponseEntity<>(HttpStatus.OK);
    }


}
