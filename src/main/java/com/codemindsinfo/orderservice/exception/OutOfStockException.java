package com.codemindsinfo.orderservice.exception;

public class OutOfStockException extends Exception {

    public OutOfStockException(String message, Throwable cause) {
        super(message, cause);
    }

    public OutOfStockException(String message) {
        super(message);
    }
}
