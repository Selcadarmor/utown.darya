package com.example.Utown.exception;

public class RestaurantNotFoundException extends RuntimeException {
    public RestaurantNotFoundException(Long id) {
        super("Restaurant with id " + id + " not found");
    }
}
