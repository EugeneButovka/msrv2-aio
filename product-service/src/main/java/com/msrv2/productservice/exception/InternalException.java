package com.msrv2.productservice.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.client.HttpServerErrorException;

public class InternalException extends HttpServerErrorException {
    public InternalException(String message) {
        super(HttpStatus.INTERNAL_SERVER_ERROR, "Server error: "+ message);
    }
}
