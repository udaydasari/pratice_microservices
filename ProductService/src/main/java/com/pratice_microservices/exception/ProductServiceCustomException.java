package com.pratice_microservices.exception;

import lombok.Data;

@Data
public class ProductServiceCustomException extends RuntimeException {
    private String errorCode;

    public ProductServiceCustomException(String message, String errorCode) {
        super(message);
        this.errorCode = errorCode;
    }
}