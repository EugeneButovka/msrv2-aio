package com.msrv2.productservice.controller;

import com.msrv2.productservice.exception.ProductNotAvailableException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.rmi.ServerError;
import java.rmi.ServerException;
import java.util.NoSuchElementException;

@RestControllerAdvice
@Slf4j
public class GlobalExceptionHandler {

    @ExceptionHandler(value = {RuntimeException.class})
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    protected RuntimeException handleGenericError(RuntimeException ex) {
        log.error(ex.toString());
        String message = "Internal error: " + ex.getMessage();
        return new RuntimeException(message);
    }

    @ExceptionHandler(value = {ServerException.class})
    @ResponseStatus(HttpStatus.SERVICE_UNAVAILABLE)
    protected RuntimeException handleServerError(RuntimeException ex) {
        log.error(ex.toString());
        String message = "Server error: " + ex.getMessage();
        return new RuntimeException(message);
    }


    @ExceptionHandler(value = {NoSuchElementException.class})
    @ResponseStatus(HttpStatus.NOT_FOUND)
    protected RuntimeException handleNoSuchElement(RuntimeException ex) {
        String message = "NO such element: " + ex.getMessage();
        return new RuntimeException(message) {
            @Override
            public synchronized Throwable fillInStackTrace() {
                return this;
            }
        };
    }

    @ExceptionHandler(value = {ProductNotAvailableException.class})
    @ResponseStatus(HttpStatus.CONFLICT)
    protected RuntimeException handleProductNotAvailable(RuntimeException ex) {
        String message = "Not available: " + ex.getMessage();
        return new RuntimeException(message);
    }
}
