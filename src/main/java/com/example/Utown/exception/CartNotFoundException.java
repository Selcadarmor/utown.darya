package com.example.Utown.exception;

public class CartNotFoundException extends RuntimeException {
  public CartNotFoundException(String message) {
    super(message);
  }
}
