package com.msrv2.catalogservice.controller;

import com.msrv2.catalogservice.exception.ProductNotFoundException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.util.NoSuchElementException;

@ControllerAdvice
public class RestResponseEntityExceptionHandler
        extends ResponseEntityExceptionHandler {

    @ExceptionHandler(value = {RuntimeException.class})
    protected ResponseEntity<Object> handleGenericError(RuntimeException ex, WebRequest request) {
        String bodyOfResponse = "Internal error";
        return handleExceptionInternal(ex, bodyOfResponse,
                                       new HttpHeaders(), HttpStatus.CONFLICT, request);
    }

    @ExceptionHandler(value = {NoSuchElementException.class, ProductNotFoundException.class})
    protected ResponseEntity<Object> handleNoSuchElement(RuntimeException ex, WebRequest request) {
        String bodyOfResponse = "NO such element: " + ex.getMessage();
        return handleExceptionInternal(ex, bodyOfResponse,
                                       new HttpHeaders(), HttpStatus.NOT_FOUND, request);
    }
}
