package com.example.Utown.exception;

public class AccessDeniedToOrderException extends RuntimeException {
    public AccessDeniedToOrderException(Long orderId) {
        super("You can cancel only your orders. Access denied for order ID: " + orderId);
    }
}
