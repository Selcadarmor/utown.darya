package com.example.Utown.exception;

public class RestaurantAlreadyFavoritedException extends RuntimeException {
    public RestaurantAlreadyFavoritedException(String restaurantName) {
        super("Restaurant " + restaurantName + " is already in favorites");
    }
}

