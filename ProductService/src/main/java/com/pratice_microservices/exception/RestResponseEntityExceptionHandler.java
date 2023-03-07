package com.pratice_microservices.exception;

import com.pratice_microservices.model.ErrorResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;
@ControllerAdvice
public class RestResponseEntityExceptionHandler  extends ResponseEntityExceptionHandler {
    @ExceptionHandler(ProductServiceCustomException.class)
    public ResponseEntity<ErrorResponse> handleProductServiceException(ProductServiceCustomException productServiceCustomException) {
        return new ResponseEntity<>(new ErrorResponse().builder()
                .errorMessage(productServiceCustomException.getMessage())
                .errorCode(productServiceCustomException.getErrorCode())
                .build(), HttpStatus.NOT_FOUND);
    }
}