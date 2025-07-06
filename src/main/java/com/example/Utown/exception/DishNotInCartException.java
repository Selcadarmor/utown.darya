package com.example.Utown.exception;

public class DishNotInCartException extends RuntimeException {
  public DishNotInCartException(Long dishId) {
    super("Dish with id " + dishId + " not found in cart");
  }

  public DishNotInCartException(String message) {
    super(message);
  }
}
