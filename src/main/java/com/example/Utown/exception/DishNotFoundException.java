package com.example.Utown.exception;

public class DishNotFoundException extends RuntimeException {
    public DishNotFoundException(Long id) {
        super("Dish with id " + id + " not found");
    }
}
