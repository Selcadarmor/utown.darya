package com.example.Utown.exception;

public class RestaurantNotInFavoritesException extends RuntimeException {
    public RestaurantNotInFavoritesException(String restaurantName) {
        super("Restaurant " + restaurantName + " is not in favorites");
    }
}
