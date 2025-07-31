package com.example.Utown.exception;

public class DifferentRestaurantException extends RuntimeException {
    public DifferentRestaurantException() {
        super("The Dish is from different restaurant");
    }

    public DifferentRestaurantException(String message) {
        super(message);
    }
}

