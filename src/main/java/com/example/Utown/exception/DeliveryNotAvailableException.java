package com.example.Utown.exception;

public class DeliveryNotAvailableException extends RuntimeException {
    public DeliveryNotAvailableException(String fullAddress) {
        super("Delivery not available at " + fullAddress);
    }
}
