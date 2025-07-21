package com.example.Utown.exception;

public class InvalidDishCountException extends RuntimeException {
  public InvalidDishCountException(String message) {
    super(message);
  }
}
