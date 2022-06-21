package com.msrv2.productservice.exception;

public class ProductNotAvailableException extends RuntimeException {
    public ProductNotAvailableException() {
    }

    public ProductNotAvailableException(String uniqId) {
        super("Product with id [" + uniqId + "] not available");
    }

    public ProductNotAvailableException(String message, Throwable cause) {
        super(message, cause);
    }

    public ProductNotAvailableException(Throwable cause) {
        super(cause);
    }
}
